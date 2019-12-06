package com.example.clienteasn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clienteasn.Activities.BuscarAmigoActivity;
import com.example.clienteasn.Activities.RegisterActivity;
import com.example.clienteasn.MainActivity;
import com.example.clienteasn.R;

public class ChatFragment extends Fragment{

    private Button btnAmigos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        btnAmigos = v.findViewById(R.id.btnBuscar);


        btnAmigos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BuscarAmigoActivity.class);
                v.getContext().startActivity(intent);
            }

        });
        return v;
    }

}
