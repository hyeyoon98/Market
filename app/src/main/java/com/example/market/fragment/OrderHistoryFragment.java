package com.example.market.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.market.OrderList.OrderItem;
import com.example.market.OrderList.OrderListAdapter;
import com.example.market.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

    public class OrderHistoryFragment extends ListFragment {

        public final String DATA_STORE = "DATA_STORE";
        private String profileID = "profile_id";
        private ListView mListView;

        public OrderHistoryFragment() {
        }

        public static com.example.market.fragment.OrderHistoryFragment newInstance() {
            com.example.market.fragment.OrderHistoryFragment orderHistoryFragment = new com.example.market.fragment.OrderHistoryFragment();
            //데이터 전달
            Bundle bundle = new Bundle();
            orderHistoryFragment.setArguments(bundle);
            return orderHistoryFragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            volleyPost();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_orderhistory, container, false);

            mListView = (ListView) view.findViewById(android.R.id.list);
            mListView.addHeaderView(new View(getActivity()));
            mListView.addFooterView(new View(getActivity()));
           // ConstraintLayout btnOrder = (ConstraintLayout) view.findViewById(R.id.layout_btn_order);

            /*btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    startActivity(intent);
                }
            });*/

            return view;
        }

        public void volleyPost() {

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

        public void setPreference(String key, String value){
            SharedPreferences pref = getContext().getSharedPreferences(DATA_STORE, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public String getPreferenceString(String key) {
            SharedPreferences pref = getContext().getSharedPreferences(DATA_STORE, MODE_PRIVATE);
            return pref.getString(key, "");
        }

        //리스트뷰에 넣기
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

                    //마켓 이름 저장
                    setPreference(profileID,jObject.getString("market_name") );

                    order.setMarketName(jObject.getString("market_name"));
                    order.setOrderContent(jObject.getString("order_content"));
                    order.setDate(setDateForm(jObject.getString("regist_date")));
                    mList.add(order);
                }
                mListView.setAdapter(new OrderListAdapter(mList));

            } catch(JSONException e) {
                e.printStackTrace();
            }
        }


        //날짜 형식 변경
        public String setDateForm(String date) {
            String newDate = null;
            SimpleDateFormat receiveDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat changeDate = new SimpleDateFormat("yyyy-MM-dd\nHH:mm");

            try {
                Date receiveForm = receiveDate.parse(date);
                newDate = changeDate.format(receiveForm);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return newDate;
        }

        public void refreshFragment(Fragment fragment) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }


