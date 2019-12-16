package com.example.clienteasn.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clienteasn.R;
import com.example.clienteasn.callback.ServerCallBack;
import com.example.clienteasn.model.AmigoBusqueda;
import com.example.clienteasn.model.Reaccion;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuscarAmigoActivity extends AppCompatActivity implements Validator.ValidationListener {

    private String idUsuario;
    private String idCuenta;
    @NotEmpty
    private EditText txtBuscarAmigos;
    private TextView txtUsuarioBusqueda;
    private TextView txtNombreBusqueda;
    private TextView txtApellidoBusqueda;
    private Button btnAgregar;
    private Button btnBuscarAmigo;
    private FrameLayout frameResult;

    private ArrayList<String> listaAmigos;
    protected AmigoBusqueda amigoPorAgregar;

    private Validator validator;

    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    String TAG = "BuscarAmigoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_amigo);

        volley = VolleyS.getInstance(this);
        fRequestQueue = volley.getRequestQueue();

        validator = new Validator(this);
        validator.setValidationListener(this);

        txtBuscarAmigos = findViewById(R.id.txtBuscarAmigos);
        txtUsuarioBusqueda = findViewById(R.id.txtUsuarioBusqueda);
        txtNombreBusqueda = findViewById(R.id.txtNombreBusqueda);
        txtApellidoBusqueda = findViewById(R.id.txtApellidoBusqueda);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnBuscarAmigo = findViewById(R.id.btnBuscarAmigo);
        btnBuscarAmigo.setEnabled(false);
        frameResult = findViewById(R.id.frameResult);

        Default d = Default.getInstance(BuscarAmigoActivity.this);

        idUsuario = d.getUsuario();
        idCuenta = d.getCuenta();

        ServerCallBack serverCallBack = new ServerCallBack() {
            @Override
            public void setListaAmigos(ArrayList<String> listaAmigosCB) {
                listaAmigos = listaAmigosCB;
                btnBuscarAmigo.setEnabled(true);
            }
        };

        btnBuscarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnAgregar.getText().toString().compareTo("Agregar") == 0) {
                    try{
                        agregarAmigo();
                    } catch (JSONException e) {
                        Log.e(TAG, "Testing network");
                    }
                }
            }
        });

        getAmigosUsuario(serverCallBack);

    }

    public void agregarAmigo() throws JSONException{
        JSONObject solicitud = new JSONObject();
        solicitud.put("usuarioEnviaId", idUsuario);
        solicitud.put("usuarioRecibeId", amigoPorAgregar.getIdUsuario());

        JsonObjectRequest agregarAmigoRequest = new JsonObjectRequest(Request.Method.POST, ApiEndpoint.agregarAmigo,
                solicitud, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listaAmigos.add(amigoPorAgregar.getIdUsuario());
                btnAgregar.setText("Agregado");
                Toast.makeText(BuscarAmigoActivity.this, "Se ha agregado a " + amigoPorAgregar.getUsuario() +
                        " a tu lista de amigos", Toast.LENGTH_SHORT).show();
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
        );

        volley.addToQueue(agregarAmigoRequest);
    }

    public void buscarAmigo() {
        String busquedaUsuario = txtBuscarAmigos.getText().toString();
        JsonObjectRequest usuarioBuscarRequest = new JsonObjectRequest(Request.Method.GET, ApiEndpoint.obtenerCuenta + "/" + busquedaUsuario, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            AmigoBusqueda result = JsonAdapter.cuentaAdapter(response);
                            setResultadoFrame(result);
                        } catch (JSONException e) {
                            Log.e(TAG, "Cannot parse JSON response");
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Imposible buscar usuario");
                    }
                }
                );
        volley.addToQueue(usuarioBuscarRequest);
    }

    private void setResultadoFrame(AmigoBusqueda result) {
        boolean exists = revisarExisteAmistad(result);
        txtUsuarioBusqueda.setText(result.getUsuario());
        txtNombreBusqueda.setText(result.getNombre());
        txtApellidoBusqueda.setText(result.getApellidos());
        if(exists) {
            btnAgregar.setText("Agregado");
            btnAgregar.setEnabled(false);
        } else {
            btnAgregar.setText("Agregar");
            amigoPorAgregar = result;
        }

        frameResult.setVisibility(View.VISIBLE);
    }

    private boolean revisarExisteAmistad(AmigoBusqueda amigo) {
        boolean exists = false;
        for (int i = 0; i < listaAmigos.size(); i++) {
            if(listaAmigos.get(i).compareTo(amigo.getIdUsuario()) == 0) {
                exists = true;
            }
        }
        return exists;
    }

    public void getAmigosUsuario(final ServerCallBack serverCallBack) {
        final ArrayList<String> amigos = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(ApiEndpoint.amigosUsuario + idUsuario,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("PubFragment", response.toString());
                            for (int i = 0; i < response.length(); i++) {
                                String amigo = response.get(i).toString();
                                amigos.add(amigo);
                            }

                            serverCallBack.setListaAmigos(amigos);


                        } catch (JSONException e) {
                            Log.d(TAG, "Cannot parse JSON");
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Testing Network");
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

    @Override
    public void onValidationSucceeded() {
        if(txtNombreBusqueda.getText().toString().compareTo(idUsuario) != 0) { buscarAmigo();}
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
