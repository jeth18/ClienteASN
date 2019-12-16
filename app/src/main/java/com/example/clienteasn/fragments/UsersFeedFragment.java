package com.example.clienteasn.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.clienteasn.R;
import com.example.clienteasn.model.Publicacion;
import com.example.clienteasn.model.Reaccion;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.MyJsonArrayRequest;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.example.clienteasn.viewmodel.ClickListener;
import com.example.clienteasn.viewmodel.ModeradorRVAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UsersFeedFragment extends Fragment {

    RecyclerView recyclerView;
    ModeradorRVAdapter recyclerViewAdapter;
    ArrayList<Publicacion> rowsArrayList = new ArrayList<>();

    boolean isLoading = false;
    String idUsuario;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private String TAG = "PublicacionFragment";
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users_feed, container, false);

        recyclerView = v.findViewById(R.id.recyclerViewModerador);

        context = container.getContext();
        Default d = Default.getInstance(context);
        volley = VolleyS.getInstance(context);
        fRequestQueue = volley.getRequestQueue();

        initAdapter();
        initScrollListener();
        idUsuario = d.getUsuario();

        populatePublications(0);

        return v;
    }

    private void populatePublications(final int nextLimit) {
        JSONObject amigosObj = new JSONObject();
        try {
            amigosObj.put("inicioSegmento", nextLimit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.POST, ApiEndpoint.feedmoderador + "/", amigosObj,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("comentarios", response.getJSONObject(1).getJSONArray("comentarios").toString());
                            Log.d("reacciones", response.getJSONObject(1).getJSONArray("reacciones").toString());
                            ArrayList<Publicacion> publicaciones = JsonAdapter.publicacionAdapter(response);
                            //Log.d("Publicaciones", response.getJSONObject(0).getJSONArray("comentarios").toString());
                            for (int i = 0; i < publicaciones.size(); i++){
                                rowsArrayList.add(publicaciones.get(i));
                            }

                            if(nextLimit == 0){
                                recyclerViewAdapter.notifyDataSetChanged();
                            }else if(nextLimit > 0){
                                recyclerViewAdapter.notifyDataSetChanged();
                                isLoading = false;
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Testing Network");
                    }
                });

        fRequestQueue.add(request);
    }

    private void initAdapter() {

        Log.d("Publicaciones", rowsArrayList.toString());

        recyclerViewAdapter = new ModeradorRVAdapter(rowsArrayList, context, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {}

            @Override
            public void onEliminarClicked(int position) {
                rowsArrayList.remove(position);
            }

            @Override
            public void onReaccionarClicked(int position, Reaccion reaccion) {

            }

            @Override
            public void onDeleteReaccionClicked(int position, int postionReaccion) {

            }

            @Override
            public void onComentarioEliminado(int position) {

            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        rowsArrayList.add(null);
        recyclerView.post(new Runnable() {
            public void run() {
                recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize;

                populatePublications(nextLimit);


            }
        }, 2000);


    }

}
