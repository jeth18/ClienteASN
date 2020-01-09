package com.example.clienteasn.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clienteasn.Activities.BuscarAmigoActivity;
import com.example.clienteasn.Activities.ChatActivity;
import com.example.clienteasn.Activities.ComentarioActivity;
import com.example.clienteasn.Activities.PerfilActivity;
import com.example.clienteasn.R;
import com.example.clienteasn.callback.ServerCallBackChats;
import com.example.clienteasn.model.ChatGroup;
import com.example.clienteasn.model.Reaccion;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.example.clienteasn.viewmodel.ChatRVAdapter;
import com.example.clienteasn.viewmodel.ClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatFragment extends Fragment{

    private Button btnAmigos;
    private Button btnMiPerfil;
    private Button btnEntrar;

    RecyclerView recyclerView;
    ChatRVAdapter recyclerViewAdapter;

    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private Default d;

    private Context context;

    private String idUsuario;

    protected ArrayList<ChatGroup> chatGroups;

    private ServerCallBackChats callBackChats = new ServerCallBackChats() {
        @Override
        public void setChats(ArrayList<ChatGroup> chats) {
            chatGroups = chats;
            initAdapter();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        context = v.getContext();
        volley = VolleyS.getInstance(context);
        fRequestQueue = volley.getRequestQueue();
        recyclerView = v.findViewById(R.id.recyclerViewGroups);
        d = Default.getInstance(context);

        idUsuario = d.getUsuario();

        btnEntrar = v.findViewById(R.id.btnEntrarChat);
        btnAmigos = v.findViewById(R.id.btnBuscar);
        btnMiPerfil = v.findViewById(R.id.btnMiPerfil);

        btnMiPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PerfilActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        btnAmigos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BuscarAmigoActivity.class);
                v.getContext().startActivity(intent);
            }

        });

        getGroups(callBackChats);

        return v;

    }

    public void initAdapter(){
        recyclerViewAdapter = new ChatRVAdapter(chatGroups, context, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("idChat", chatGroups.get(position).getIdChatG());
                context.startActivity(intent);
            }

            @Override
            public void onEliminarClicked(int position) {

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

    public void getGroups(final ServerCallBackChats cb){
        JsonArrayRequest request = new JsonArrayRequest(ApiEndpoint.getChatGroupsUser + idUsuario,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<ChatGroup> chats = JsonAdapter.chatGroupsAdapter(response);
                            cb.setChats(chats);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Cannot parse comment response", Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Chat Activity", error.getMessage());
                    }
                });

        volley.addToQueue(request);
    }





}
