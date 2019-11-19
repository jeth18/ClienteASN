package com.example.clienteasn.services.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyS {
    private static VolleyS mVolleyS = null;
    private RequestQueue mRequestQueue;

    private VolleyS(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyS getInstance(Context context){
        if(mVolleyS == null){
            mVolleyS = new VolleyS(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public void addToQueue(Request request){
        if(request != null) {
            request.setTag(this);
            if(mRequestQueue == getRequestQueue());
        }
        request.setRetryPolicy(new DefaultRetryPolicy(
                6000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        mRequestQueue.add(request);
    }


}
