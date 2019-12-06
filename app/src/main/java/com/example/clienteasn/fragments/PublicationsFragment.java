package com.example.clienteasn.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clienteasn.R;
import com.example.clienteasn.callback.ServerCallBack;
import com.example.clienteasn.model.Publicacion;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.MyJsonArrayRequest;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.example.clienteasn.viewmodel.ClickListener;
import com.example.clienteasn.viewmodel.PublicacionRVAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PublicationsFragment extends Fragment {

    RecyclerView recyclerView;
    PublicacionRVAdapter recyclerViewAdapter;
    ArrayList<Publicacion> rowsArrayList = new ArrayList<>();
    private ArrayList<String> listaAmigos;

    boolean isLoading = false;
    String idUsuario;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private String TAG = "PublicacionFragment";
    Context context;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_publications, container, false);
        recyclerView = v.findViewById(R.id.recyclerViewPubs);
        Default d = Default.getInstance(v.getContext());
        context = container.getContext();

        volley = VolleyS.getInstance(v.getContext());
        fRequestQueue = volley.getRequestQueue();

        idUsuario = d.getUsuario();

        ServerCallBack serverCallBack = new ServerCallBack() {
            @Override
            public void setListaAmigos(ArrayList<String> listaAmigosCB) {
                listaAmigos = listaAmigosCB;
                populatePublications(0);
                Log.d("amigos", listaAmigos.toString());
            }

        };

        getAmigosUsuario(serverCallBack);


        return v;
    }

    private void populatePublications(final int nextLimit) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listaAmigos.size(); i++) {
            jsonArray.put(listaAmigos.get(i));
        }

        jsonArray.put(idUsuario);


        JSONObject amigosObj = new JSONObject();
        try {
            amigosObj.put("inicioSegmento", nextLimit);
            amigosObj.put("listaAmigos", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.POST, ApiEndpoint.feedfotos, amigosObj,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<Publicacion> publicaciones = JsonAdapter.publicacionAdapter(response);
                            //Log.d("Publicaciones", response.getJSONObject(0).getJSONArray("comentarios").toString());
                            for (int i = 0; i < publicaciones.size(); i++){
                                rowsArrayList.add(publicaciones.get(i));
                            }

                            if(nextLimit == 0){
                                initAdapter();
                                initScrollListener();
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

        recyclerViewAdapter = new PublicacionRVAdapter(rowsArrayList, context, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {

            }

            @Override
            public void onClicked(int position) {

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


    public void getAmigosUsuario(final ServerCallBack serverCallBack){
        final ArrayList<String> amigos = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(ApiEndpoint.amigosUsuario + idUsuario,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("PubFragment", response.toString());
                            for (int i = 0; i < response.length(); i++) {
                                String amigo = response.get(i).toString();
                                Log.d("PubFragment", amigo);
                                amigos.add(amigo);
                            }

                            serverCallBack.setListaAmigos(amigos);


                        } catch (JSONException e) {
                            Log.d("PubFragment", "Cannot parse JSON");
                        }

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public int getMethod() {
                return Method.GET;
            }
        };
        fRequestQueue.add(request);


    }
}
