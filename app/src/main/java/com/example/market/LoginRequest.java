package com.example.market;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    String postUrl = "http://211.229.250.40/api_init_session";

    public LoginRequest(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);

        JSONObject postData = new JSONObject();
        try {
            postData.put("input_id","sample");
            postData.put("input_pw", "sample");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }



   /* final static private String URL = "http://211.229.250.40/api_init_session";
    private Map<String, String> parameters;


    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener){
      super(Method.POST, URL, listener, null);

       parameters = new HashMap<>();
       parameters.put("input_id", userID);
       parameters.put("input_pw", userPassword);
       System.out.println("유저아이디>>>>>>>>>"+userID);
        System.out.println("유저비번>>>>>>>>>"+userPassword);

   }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }*/

    /*public void volleyPost(){
        String postUrl = "http://211.229.250.40/api_init_session";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("input_id","sample");
            postData.put("input_pw", "sample");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }*/
}
