package com.example.clienteasn.services.network;

import com.example.clienteasn.services.pojo.LoginPOJO;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonAdapter {
    public static LoginPOJO loginAdapter(JSONObject jsonObject) throws JSONException {
        LoginPOJO res = new LoginPOJO();
        res.setToken(jsonObject.getString("token"));
        return res;
    }
}
