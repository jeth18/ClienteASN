package com.example.clienteasn.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.clienteasn.MainActivity;
import com.example.clienteasn.R;
import com.example.clienteasn.services.network.VolleyS;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtUsuario;
    private EditText txtPassword;
    private EditText txtPasswordR;
    private EditText txtCorreo;
    private EditText txtTelefono;
    private Button btnSignUp;

    private String nombre;
    private String apellido;
    private String usuario;
    private String password;
    private String passwordr;
    private String correo;
    private String telefono;

    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getValuesOfComponents();
        volley = VolleyS.getInstance(RegisterActivity.this);
        fRequestQueue = volley.getRequestQueue();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerRequest();
            }
        });

    }

    private void getValuesOfComponents() {
        txtNombre = findViewById(R.id.txtNombre)  ;
        txtApellido = findViewById(R.id.txtApellido);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        txtPasswordR = findViewById(R.id.txtPasswordR);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtTelefono = findViewById(R.id.txtTelefono);
        btnSignUp = findViewById(R.id.btnSignUp);

        nombre = txtNombre.getText().toString();
        apellido = txtApellido.getText().toString();
        usuario = txtUsuario.getText().toString();
        password = txtPassword.getText().toString();
        passwordr = txtPasswordR.getText().toString();
        correo = txtCorreo.getText().toString();
        telefono = txtTelefono.getText().toString();
    }

    private void registerRequest(){

    }

    private String validateInputs(){
        String err = "";
        if(nombre == "" || apellido == "" || usuario == "" || password == "" || passwordr == "" || correo == "" || telefono == "") {
            err += "Por favor rellene todos los campos solicitados";
        }

        return err;
    }
}
