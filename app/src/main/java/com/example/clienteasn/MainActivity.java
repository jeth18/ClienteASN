package com.example.clienteasn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.clienteasn.Activities.AppActivity;
import com.example.clienteasn.Activities.ModeradorActivity;
import com.example.clienteasn.Activities.RegisterActivity;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.example.clienteasn.services.pojo.LoginPOJO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnSignIn;
    private Button btnSignUp;

    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private Default d;

    public static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getValuesOfComponents();
        volley = VolleyS.getInstance(MainActivity.this);
        fRequestQueue = volley.getRequestQueue();


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRequest();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void getValuesOfComponents() {
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    private void loginRequest() {
        Map<String, String> param = new HashMap<>();
        param.put("password", txtPassword.getText().toString());
        param.put("usuario", txtUsername.getText().toString());

        JSONObject jsonObject = new JSONObject(param);

        Log.d("ObjLogin", jsonObject.toString());

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                ApiEndpoint.login, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Usuario", response.toString());
                            LoginPOJO result = JsonAdapter.loginAdapter(response);
                            d = Default.getInstance(MainActivity.this);
                            d.setToken(result.getToken());
                            d.setCuenta(result.getCuenta());
                            d.setUsuario(result.getUsuario());
                            Log.d("Usuario2", response.toString());

                            if(result.isModerador() == false){
                                Log.d("Usuario3", response.toString());
                                Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, AppActivity.class);
                                MainActivity.this.startActivity(intent);
                                finish();
                            } else {
                                Log.d("Usuario4", response.toString());
                                Toast.makeText(MainActivity.this, "Bienvenido Moderador", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, ModeradorActivity.class);
                                MainActivity.this.startActivity(intent);
                                finish();
                            }


                            finish();

                        } catch (JSONException e) {

                            Toast.makeText(MainActivity.this, "Cannot parse response", Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Testing Network");
                    }
                }
        );

        volley.addToQueue(jsonRequest);
    }
}
