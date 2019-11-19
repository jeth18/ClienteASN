package com.example.clienteasn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
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

    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    public static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        volley = VolleyS.getInstance(MainActivity.this);
        fRequestQueue = volley.getRequestQueue();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRequest();
            }
        });
    }

    private void loginRequest() {
        Map<String, String> param = new HashMap<>();
        param.put("password", txtPassword.getText().toString());
        param.put("correo", txtUsername.getText().toString());

        JSONObject jsonObject = new JSONObject(param);

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                ApiEndpoint.login, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            LoginPOJO result = JsonAdapter.loginAdapter(response);
                            Default d = Default.getInstance(MainActivity.this);
                            d.setToken(result.getToken());
                            Toast.makeText(MainActivity.this, "TK" + d.getToken(), Toast.LENGTH_SHORT).show();
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
