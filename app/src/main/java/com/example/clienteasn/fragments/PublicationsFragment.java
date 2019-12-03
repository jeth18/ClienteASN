package com.example.clienteasn.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.clienteasn.Activities.AppActivity;
import com.example.clienteasn.MainActivity;
import com.example.clienteasn.R;
import com.example.clienteasn.callback.ServerCallBack;
import com.example.clienteasn.crud.SolicitudCRUD;
import com.example.clienteasn.model.Publicacion;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.example.clienteasn.viewmodel.PublicacionRVAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class PublicationsFragment extends Fragment {

    RecyclerView recyclerView;
    PublicacionRVAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    private ArrayList<String> listaAmigos;

    boolean isLoading = false;
    String idUsuario;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private String TAG = "PublicacionFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_publications, container, false);
        recyclerView = v.findViewById(R.id.recyclerViewPubs);
        Default d = Default.getInstance(v.getContext());

        volley = VolleyS.getInstance(v.getContext());
        fRequestQueue = volley.getRequestQueue();

        idUsuario = d.getUsuario();
        ServerCallBack serverCallBack = new ServerCallBack() {
            @Override
            public void setListaAmigos(ArrayList<String> listaAmigosCB) {
                listaAmigos = listaAmigosCB;
                initAdapter();
                initScrollListener();
                Log.d("amigos", listaAmigos.toString());
                populatePublications();
            }
        };
        getAmigosUsuario(serverCallBack);
        return v;
    }

    private void populatePublications() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listaAmigos.size(); i++) {
            jsonArray.put(listaAmigos.get(i));
        }

        JSONObject amigosObj = new JSONObject();
        try {
            amigosObj.put("inicioSegmento", 0);
            amigosObj.put("listaAmigos", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("ObjetoJSON", amigosObj.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiEndpoint.feedfotos, amigosObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resJsonArray = new JSONArray(response);
                            ArrayList<Publicacion> result = JsonAdapter.publicacionAdapter(resJsonArray);

                            for (int i = 0; i < result.size(); i++) {
                                Log.d("Publicacion " + i, result.get(i).toString());
                            }

                        } catch (JSONException e) {
                            Log.d("PubFragment", "Cannot parse JSON");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getMessage());
                    }
                }

        );

        fRequestQueue.add(jsonObjectRequest);
    }

    private void initAdapter() {

        recyclerViewAdapter = new PublicacionRVAdapter(rowsArrayList);
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
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
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
