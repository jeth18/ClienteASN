package com.example.clienteasn.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clienteasn.R;
import com.example.clienteasn.callback.ServerCallBack;
import com.example.clienteasn.callback.SeverCallBackMessages;
import com.example.clienteasn.model.ChatGroup;
import com.example.clienteasn.model.Comentario;
import com.example.clienteasn.model.Mensaje;
import com.example.clienteasn.model.Reaccion;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.MyJsonArrayRequest;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.example.clienteasn.viewmodel.ClickListener;
import com.example.clienteasn.viewmodel.MensajeRVAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import example.ChatOuterClass;
import example.ChatGrpc;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MensajeRVAdapter recyclerViewAdapter;
    String idMensaje;
    String idUsuario;
    String idCuenta;
    ChatGroup chatGroup;
    private EditText txtMensajeNuevo;
    private ImageButton btnEnviar;
    private ArrayList<Mensaje> mensajes;

    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private String TAG = "ChatActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        volley = VolleyS.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        Default d = Default.getInstance(this);

        idUsuario = d.getUsuario();
        idCuenta = d.getCuenta();

        btnEnviar = findViewById(R.id.btnEnviarM);
        txtMensajeNuevo = findViewById(R.id.editTextComentario);
        recyclerView = findViewById(R.id.recyclerViewComment);
        SeverCallBackMessages severCallBackMessages = new SeverCallBackMessages() {
            @Override
            public void setListaMessages(ArrayList<Mensaje> listaMessages) {

            }
        };
        getGrupo(severCallBackMessages);


        initAdapter();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
            }
        });
        Log.d("Mensajes", mensajes.toString());

    }

    private void initAdapter() {

        recyclerViewAdapter = new MensajeRVAdapter(mensajes, this, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {}

            @Override
            public void onEliminarClicked(int position) {}

            @Override
            public void onReaccionarClicked(int position, Reaccion reaccion) {}

            @Override
            public void onDeleteReaccionClicked(int position, int postionReaccion) {}

            @Override
            public void onComentarioEliminado(int position) {}
        });

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void getMessages(final SeverCallBackMessages severCallBackMessages) {
        Bundle datos = this.getIntent().getExtras();
        String idChat = datos.getString("idChat");

        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET,ApiEndpoint.getMessages + idChat, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            mensajes = JsonAdapter.mensajesAdapter(response);

                        } catch (JSONException e) {
                            Log.d(TAG, "Cannot parse JSON");
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public int getMethod() {
                return Method.GET;
            }
        };
        volley.addToQueue(request);
    }

    public void getGrupo(final SeverCallBackMessages severCallBackMessages) {
        Bundle datos = this.getIntent().getExtras();
        String idChat = datos.getString("idChat");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,ApiEndpoint.getChatGroup + idChat, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            chatGroup = JsonAdapter.chatGroupAdapter(response);

                        } catch (JSONException e) {
                            Log.d(TAG, "Cannot parse JSON");
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error");
                    }
                }
        ) {
            @Override
            public int getMethod() {
                return Method.GET;
            }
        };
        volley.addToQueue(request);
    }
    private void enviarMensaje(){
        String mensaje = txtMensajeNuevo.getText().toString();
        Bundle datos = this.getIntent().getExtras();
        String idChat = datos.getString("idChat");

        JSONObject jsonObject = new JSONObject();

        try {
            /*
               "text": "Mensaje prueba",
                "memberId": "5dea9c58306b6f261cd57fec",
                "chatgroupId": "5e12407f23eb9d4278c8fa0d"
             */
            jsonObject.put("text", mensaje);
            jsonObject.put("memberId", idUsuario);
            jsonObject.put( "chatgroupId", idChat);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ApiEndpoint.sendMessage,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("Mensaje nuevo", response.toString());

                            Mensaje result = JsonAdapter.mensajeAdapter(response);
                            mensajes.add(result);

                            txtMensajeNuevo.setText("");

                            refresh();
                        } catch (JSONException e) {

                            Toast.makeText(ChatActivity.this, "Cannot parse comment response", Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        volley.addToQueue(jsonObjectRequest);

    }

    private void refresh(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int scrollPosition = mensajes.size();
                Log.d("Tamaño", mensajes.toString());
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }


}
