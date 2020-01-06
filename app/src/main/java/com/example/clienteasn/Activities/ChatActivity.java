package com.example.clienteasn.Activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.clienteasn.R;

import android.os.Bundle;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    public void enviarMensaje(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("192.168.43.42", 50051)
                .usePlaintext().build();
    }
}
