package com.example.market.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.market.MainActivity;
import com.example.market.OrderList.OrderItem;
import com.example.market.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class OrderFragment extends Fragment {


    public final String DATA_STORE = "DATA_STORE";

    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        OrderFragment orderFragment = new OrderFragment();
        //데이터 전달
        Bundle bundle = new Bundle();
        orderFragment.setArguments(bundle);
        return orderFragment;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        ConstraintLayout btnUpload = view.findViewById(R.id.btn_upload);

        EditText editOrder = view.findViewById(R.id.edit_order);/*
        String contents = editOrder.getText().toString();
        contents = contents.replace("'", "''");*/



        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editOrder.getText().toString();
                if (content.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("알림")
                            .setMessage("주문할 재료를 입력바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                } else {
                    volleyPost();
                }
            }
        });

        return view;
    }


////////////////////여기부터


    public void volleyPost() {

        String postUrl = "http://211.229.250.40/api_create_order"; // 서버 주소
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JSONObject postData = new JSONObject();
        String token = "token";

        try {
            EditText editOrder = getActivity().findViewById(R.id.edit_order);
            String contents = editOrder.getText().toString();
            contents = contents.replace("'", "''");

            postData.put("order_content", contents);

        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                List<OrderItem> orderItemList = new ArrayList<>();
                String success = "200";

                try {
                    if (response.getString("result").equals(success)) {
                        System.out.println("응답해 >>>>>>>>>>" + success);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("알림")
                                .setMessage("주문이 정상접수되었습니다.\n주문해주셔서 감사합니다:)")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .create()
                                .show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", getPreferenceString(token));
                return headers;
            }


        };

        requestQueue.add(jsonObjectRequest); // 서버에 요청

    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getContext().getSharedPreferences(DATA_STORE, MODE_PRIVATE);
        return pref.getString(key, "");
    }
}

