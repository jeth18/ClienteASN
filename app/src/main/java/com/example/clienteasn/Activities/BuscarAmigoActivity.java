package com.example.clienteasn.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.clienteasn.R;
import com.example.clienteasn.services.persistence.Default;

public class BuscarAmigoActivity extends AppCompatActivity {

    private String idUsuario;
    private String idCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_amigo);

        Default d = Default.getInstance(BuscarAmigoActivity.this);

        idUsuario = d.getUsuario();
        idCuenta = d.getCuenta();
    }



}
