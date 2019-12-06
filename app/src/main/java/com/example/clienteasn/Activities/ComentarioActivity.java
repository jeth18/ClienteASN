package com.example.clienteasn.Activities;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clienteasn.MainActivity;
import com.example.clienteasn.model.Comentario;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.example.clienteasn.viewmodel.ClickListener;
import com.example.clienteasn.viewmodel.ComentarioRVAdapter;
import com.example.clienteasn.viewmodel.PublicacionRVAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.clienteasn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComentarioActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ComentarioRVAdapter recyclerViewAdapter;
    ArrayList<Comentario> comentarios;
    String idPublicacion;
    String idUsuario;
    String idCuenta;
    private EditText txtComentarioNuevo;
    private ImageButton btnPublicar;

    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);
        comentarios = (ArrayList<Comentario>) getIntent().getSerializableExtra("comentarios");
        idPublicacion = (String) getIntent().getSerializableExtra("idPublicacion");

        volley = VolleyS.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        Default d = Default.getInstance(this);

        idUsuario = d.getUsuario();
        idCuenta = d.getCuenta();
        btnPublicar = findViewById(R.id.btnComment);
        txtComentarioNuevo = findViewById(R.id.editTextComentario);
        recyclerView = findViewById(R.id.recyclerViewComment);

        initAdapter();

        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarComentario();
            }
        });
        Log.d("comentarios", comentarios.toString());

    }

    private void initAdapter() {

        recyclerViewAdapter = new ComentarioRVAdapter(comentarios, this, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {

            }

            @Override
            public void onClicked(int position) {

            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void publicarComentario(){
        String comentario = txtComentarioNuevo.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("comentario", comentario);
            jsonObject.put("idUsuario", idUsuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ApiEndpoint.nuevoComentario + "/" + idPublicacion,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("comentario nuevo", response.toString());

                            Comentario result = JsonAdapter.comentarioAdapter(response);
                            comentarios.add(result);

                            txtComentarioNuevo.setText("");

                            refresh();
                        } catch (JSONException e) {

                            Toast.makeText(ComentarioActivity.this, "Cannot parse comment response", Toast.LENGTH_SHORT).show();

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
                int scrollPosition = comentarios.size();
                Log.d("Tamaño", comentarios.toString());
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

}
