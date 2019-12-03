package com.example.clienteasn.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.clienteasn.MainActivity;
import com.example.clienteasn.R;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.pojo.RegisterPOJO;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(trim = true, message = "Por favor ingrese su nombre")
    private EditText txtNombre;

    @NotEmpty(trim = true, message = "Por favor ingrese sus apellidos")
    private EditText txtApellido;

    @NotEmpty(trim = true, message = "Por favor ingrese un nombre de usuario")
    private EditText txtUsuario;

    @NotEmpty(trim = true, message = "Por favor ingrese un correo electronico")
    @Email(message = "Correo invalido (example@domain.com)")
    private EditText txtCorreo;

    @NotEmpty(trim = true, message = "Por favor ingrese una contraseña")
    @Password(min = 8, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE,
            message = "La contraseña debe ser una combinación de caracteres alfanumericos")
    private EditText txtPassword;

    @NotEmpty(trim = true, message = "Por favor confirma la contraseña")
    @ConfirmPassword(message = "Las contraseñas no coinciden")
    private EditText txtPasswordR;

    @NotEmpty(trim = true, message = "Por favor ingrese un numero de celular")
    @Length(min = 10, max = 10, message = "Ingrese un numero valido de 10 digitos")
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
    private Validator validator;
    protected RequestQueue fRequestQueue;

    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        validator = new Validator(this);
        validator.setValidationListener(this);
        btnSignUp = findViewById(R.id.btnSignUp);


        volley = VolleyS.getInstance(RegisterActivity.this);
        fRequestQueue = volley.getRequestQueue();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesOfComponents();
                validator.validate();
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

        nombre = txtNombre.getText().toString();
        apellido = txtApellido.getText().toString();
        usuario = txtUsuario.getText().toString();
        password = txtPassword.getText().toString();
        passwordr = txtPasswordR.getText().toString();
        correo = txtCorreo.getText().toString();
        telefono = txtTelefono.getText().toString();
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Campos validos", Toast.LENGTH_LONG).show();
        registerNewUser();

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

    protected void registerNewUser(){
        Map<String, String> param = new HashMap<>();
        param.put("nombre", nombre);
        param.put("apellido", apellido);
        param.put("password", password);
        param.put("usuario", usuario);
        param.put("telefono", telefono);
        param.put("correo", correo);

        JSONObject registerJsonObject = new JSONObject(param);

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                ApiEndpoint.register, registerJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            RegisterPOJO result = JsonAdapter.registerAdapter(response);
                            Toast.makeText(RegisterActivity.this, "Usuario: " + result.getUsuario() + " registrado con exito", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            RegisterActivity.this.startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            Toast.makeText(RegisterActivity.this, "Cannot parse response", Toast.LENGTH_SHORT).show();
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
