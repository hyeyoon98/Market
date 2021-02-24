package com.example.market.OrderList;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Order {
    private static Order instance;
    private RequestQueue requestQueue;

    public static Order getInstance(Context context) {
        if(instance == null)
            instance = new Order(context);
        return instance;
    }

    public Order(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

}
