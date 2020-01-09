package com.example.clienteasn.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clienteasn.R;
import com.example.clienteasn.callback.SeverCallBackMessages;
import com.example.clienteasn.model.ChatGroup;
import com.example.clienteasn.model.Mensaje;
import com.example.clienteasn.model.Reaccion;
import com.example.clienteasn.services.network.AndroidStreamObserver;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.MyJsonArrayRequest;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.network.gRPCEndPoint;
import com.example.clienteasn.services.persistence.Default;
import com.example.clienteasn.viewmodel.ClickListener;
import com.example.clienteasn.viewmodel.MensajeRVAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import example.ChatGrpc;
import example.ChatOuterClass;
import example.ChatOuterClass.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MensajeRVAdapter recyclerViewAdapter;
    String idMensaje;
    String idUsuario;
    String idCuenta;
    String usuario;
    String idChat;
    ChatGroup chatGroup;
    private EditText txtMensajeNuevo;
    private ImageButton btnEnviar;
    private ArrayList<Mensaje> mensajes;

    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private String TAG = "ChatActivity";

    private ManagedChannel mChan;
    private StreamObserver<ChatOuterClass.Message> req;
    private ChatGrpc.ChatStub stub;
    private ChatGrpc.ChatStub bStub;





    final SeverCallBackMessages severCallBackMessages = new SeverCallBackMessages() {
        @Override
        public void setListaMessages(ArrayList<Mensaje> listaMessages) {
            mensajes = listaMessages;
            Log.d("Adaptador", "Prueba Adaptador");
            joinChat();
            initAdapter();
        }

        @Override
        public void afterGetGrupo() {
            getMessages(severCallBackMessages);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        volley = VolleyS.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        Default d = Default.getInstance(this);

        idUsuario = d.getUsuario();
        idCuenta = d.getCuenta();
        usuario = d.getUserName();

        Bundle datos = this.getIntent().getExtras();
        idChat = datos.getString("idChat");

        btnEnviar = findViewById(R.id.btnEnviarM);
        txtMensajeNuevo = findViewById(R.id.editTextChat);
        recyclerView = findViewById(R.id.recyclerViewMessages);

        mChan = ManagedChannelBuilder.forAddress(gRPCEndPoint.mHostIp, gRPCEndPoint.mPort)
                .usePlaintext(true)
                .build();

        stub = ChatGrpc.newStub(mChan);
        bStub = ChatGrpc.newStub(mChan);

        getGrupo(severCallBackMessages);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
            }
        });



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

        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET,ApiEndpoint.getMessages + idChat, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            ArrayList<Mensaje> result = JsonAdapter.mensajesAdapter(response);
                            severCallBackMessages.setListaMessages(result);

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,ApiEndpoint.getChatGroup + idChat, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            chatGroup = JsonAdapter.chatGroupAdapter(response);
                            severCallBackMessages.afterGetGrupo();

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

    private void joinChat(){
        StreamObserver<Message> requestObserver = stub.join(new StreamObserver<Message>() {
            @Override
            public void onNext(Message value) {
                Log.d("Prueba", "Entrada al on Next");
                Log.d("ExternalID", value.getChatgroup().getExternalId());
                Log.d("ID", idChat);
                Log.d("Respuesta", value.toString());
                if(value.getChatgroup().getExternalId().compareTo(idChat) == 0 &&
                value.getMember().getExternalId().compareTo(idUsuario) != 0) {
                    Log.d("Prueba", "Entrada al if");
                    Mensaje mensaje = new Mensaje();
                    mensaje.setId(value.getExternalId());
                    mensaje.setIdChatG(value.getChatgroup().getExternalId());
                    mensaje.setIdUsuario(value.getMember().getExternalId());
                    mensaje.setNombreUsuario(value.getMember().getUsername());
                    mensaje.setTextMensaje(value.getText());

                    mensajes.add(mensaje);
                    recyclerViewAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        });

        Message message = Message.newBuilder()
                .build();

        requestObserver.onNext(message);

    }

    private void enviarMensaje(){
        String mensaje = txtMensajeNuevo.getText().toString();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("text", mensaje);
            jsonObject.put("memberId", idUsuario);
            jsonObject.put( "chatgroupId", idChat);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiEndpoint.sendMessage,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Mensaje result = JsonAdapter.mensajeAdapter(response);
                            result.setNombreUsuario(usuario);
                            mensajes.add(result);
                            txtMensajeNuevo.setText("");
                            refresh();


                            //Mandar con grpc

                            ChatOuterClass.ChatGroup messageChatGroup = ChatOuterClass.ChatGroup.newBuilder()
                                    .setExternalId(chatGroup.getIdChatG())
                                    .build();

                            ChatOuterClass.Member messageMember = ChatOuterClass.Member.newBuilder()
                                    .setExternalId(idUsuario)
                                    .setUsername(usuario)
                                    .build();

                            Message message = Message.newBuilder()
                                    .setExternalId(result.getId())
                                    .setChatgroup(messageChatGroup)
                                    .setMember(messageMember)
                                    .setText(result.getTextMensaje())
                                    .build();

                            try{
                                bStub.send(message, new StreamObserver<Message>() {
                                    @Override
                                    public void onNext(Message value) {

                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onCompleted() {
                                        Log.d("Completed", "completado");
                                    }
                                });
                            }catch(Exception e) {
                                Log.e("ChatActivity", e.getMessage());
                            }


                            Log.d("Mensaje nuevo", response.toString());
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
        }, 1000);
    }


}
