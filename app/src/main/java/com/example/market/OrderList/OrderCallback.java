package com.example.market.OrderList;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OrderCallback extends StringRequest {

    final static private String URL = "https://211.229.250.40:8000/";
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
    }
}
