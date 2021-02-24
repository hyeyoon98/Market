package com.example.market.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.market.LoginActivity;
import com.example.market.MainActivity;
import com.example.market.OrderActivity;
import com.example.market.OrderList.Order;
import com.example.market.OrderList.OrderCallback;
import com.example.market.OrderList.OrderItem;
import com.example.market.OrderList.OrderListAdapter;
import com.example.market.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Tag;

import static android.content.Context.MODE_PRIVATE;

public class OrderFragment extends ListFragment {


    public final String DATA_STORE = "DATA_STORE";
    private ListView mListView;

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
        volleyPost();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.addHeaderView(new View(getActivity()));
        mListView.addFooterView(new View(getActivity()));
        ConstraintLayout btnOrder = (ConstraintLayout) view.findViewById(R.id.layout_btn_order);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void volleyPost(){

        String postUrl = "http://211.229.250.40/api_read_order"; // 서버 주소
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JSONObject postData = new JSONObject();
        String token = "token";



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mListView = (ListView) getActivity().findViewById(android.R.id.list);
                List<OrderItem> orderItemList = new ArrayList<>();
                String result = null;
                String success = "200";
                String error = "500";

                try {
                    if (response.getString("result").equals(success)) {
                        System.out.println("응답해 >>>>>>>>>>" + success);
                        result = response.getString("order_list");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                parsingJSONData(result);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
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

    private void parsingJSONData(String data) {
        TextView none = (TextView)getActivity().findViewById(R.id.tv_none);
        List<OrderItem> mList = new ArrayList<>();
        System.out.println("리스트 가져와 >>>>>>>>>>>" + data);
        try {
            JSONArray jArray = new JSONArray(data);
            if (jArray.length()==0) {
                none.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                // 주문 내역이 비었습니다.
            }
            for(int i = 0; i < jArray.length(); i++) {
                mListView.setVisibility(View.VISIBLE);
                OrderItem order = new OrderItem();
                JSONObject jObject = jArray.getJSONObject(i);
                order.setMarketName(jObject.getString("market_name"));
                order.setOrderContent(jObject.getString("order_content"));
                order.setDate(jObject.getString("regist_date"));
                mList.add(order);
            }
            mListView.setAdapter(new OrderListAdapter(mList));

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
