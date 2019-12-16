package com.example.clienteasn.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clienteasn.MainActivity;
import com.example.clienteasn.R;
import com.example.clienteasn.model.Cuenta;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.mobsandgeeks.saripaar.Validator;

import org.json.JSONException;
import org.json.JSONObject;

public class PerfilActivity extends AppCompatActivity {

    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private TextView tvwNombre;
    private TextView tvwApellido;
    private TextView tvwUsuario;
    private TextView tvwCorreo;
    private Button btnModificar;
    private Default d;

    private String TAG = "PerfilActivity";

    private String idCuenta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        d = Default.getInstance(this);
        idCuenta = d.getCuenta();

        volley = VolleyS.getInstance(PerfilActivity.this);

        fRequestQueue = volley.getRequestQueue();

        tvwNombre = findViewById(R.id.tvwNombre);
        tvwApellido = findViewById(R.id.tvwApellido);
        tvwUsuario = findViewById(R.id.tvwUsuario);
        tvwCorreo = findViewById(R.id.tvwCorreo);
        btnModificar = findViewById(R.id.btnModificar);
        obtenerMiCuenta();
    }

    private void obtenerMiCuenta() {
        Log.d("Prueba", "prueba");
        JsonObjectRequest miCuentaRequest = new JsonObjectRequest(Request.Method.GET,
                ApiEndpoint.miCuenta + "/" + idCuenta, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Prueba", "prueba");
                            Cuenta cuenta = JsonAdapter.miCuentaAdapter(response);
                            setInfoPerfil(cuenta);
                        } catch (JSONException e) {
                            Toast.makeText(PerfilActivity.this, "Cannot parse json cuenta", Toast.LENGTH_SHORT);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Testing network miCuenta");
                    }
                }
        );
        volley.addToQueue(miCuentaRequest);
    }

    private void setInfoPerfil(Cuenta cuenta) {
        tvwNombre.setText(cuenta.getNombre());
        tvwApellido.setText(cuenta.getApellido());
        tvwCorreo.setText(cuenta.getCorreo());
        tvwUsuario.setText(cuenta.getUsuario());
    }



}
