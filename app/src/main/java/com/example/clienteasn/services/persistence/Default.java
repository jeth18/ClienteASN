package com.example.clienteasn.services.persistence;

import android.content.Context;
import android.content.SharedPreferences;

public class Default {
    private static Default instance = null;
    private SharedPreferences preferences;

    private Default(Context context){
        this.preferences = context.getSharedPreferences("clienteasnpreferences", Context.MODE_PRIVATE);

    }

    public static Default getInstance(Context context) {
        if(instance == null) {
            instance = new Default(context);
        }
        return instance;
    }

    public String getToken(){
        return preferences.getString("token", "");

    }

    public void setToken(String token){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }
}
