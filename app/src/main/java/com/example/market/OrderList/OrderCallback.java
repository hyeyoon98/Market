package com.example.market.OrderList;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class OrderCallback {

    private static OrderCallback instance;
    private RequestQueue requestQueue;

    public static OrderCallback getInstance(Context context) {
        if(instance == null)
            instance = new OrderCallback(context);
        return instance;
    }

    private OrderCallback(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
   /* final static private String URL = "https://211.229.250.40:8000/";
    private Map<String, String> parameters;

    public OrderCallback(String marketName, String orderContent, String date, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("market_name", marketName);
        parameters.put("order_content", orderContent);
        parameters.put("regist_date", date);
    }

    protected Map<String, String> getParam() throws AuthFailureError{
        return parameters;
    }*/
}
