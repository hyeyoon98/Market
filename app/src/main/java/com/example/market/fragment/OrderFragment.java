package com.example.market.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;

import com.example.market.OrderList.OrderItem;
import com.example.market.OrderList.OrderListAdapter;
import com.example.market.OrderList.OrderListInterface;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Tag;

public class OrderFragment extends ListFragment {


    ListView listView;
    List<OrderItem> orderList;
    OrderListAdapter adapter;

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

        new BackgroundTask().excute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        orderList = new ArrayList<OrderItem>();
        adapter = new OrderListAdapter(getActivity(), orderList);
        listView.setAdapter(adapter);




        try {
            JSONObject jsonObject = new JSONObject(getArguments().getString("order_List"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");

            int count = 0;

            String marketName, orderContent, date;

            while (count < jsonArray.length()){
                JSONObject object = jsonArray.getJSONObject(count);

                marketName = object.getString("market_name");
                orderContent = object.getString("order_content");
                date = object.getString("regist_date");

                OrderItem orderItem = new OrderItem(marketName, orderContent, date);
                orderList.add(orderItem);
                count++;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }



    /*public void setRetrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://211.229.250.40:8000/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        OrderListInterface client = retrofit.create(OrderListInterface.class);
        Call<List<OrderItem>> call = client.getOrderList();

        call.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {


                System.out.println("성공");

                List<OrderItem> resource = response.body();

                ArrayList<String> arrayList = new ArrayList<>();

                for (OrderItem re:resource){
                    arrayList.add(re.getMarketName());
                    arrayList.add(re.getOrderContent());
                    arrayList.add(re.getDate());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.order_item_list);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {

                System.out.println("실패");

            }
        });
    }*/

    /*public void setRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl("https://211.229.250.40:8000/")

                .addConverterFactory(GsonConverterFactory.create())

                .build();



        final OrderListInterface orderListInterface = retrofit.create(OrderListInterface.class);

        Call<List<OrderItem>> call = orderListInterface.getOrderList();

        call.enqueue(new Callback<List<OrderItem>>() {



            @Override

            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {

                String test;

                try{

                    List<OrderItem> orderItems = response.body();



                    for(int i = 0; i< orderItems.size(); i++){

                        test = orderItems.get(i).getMarketName();

                        Log.d("Retrofit", "TestCode : "+ test.toString());

                        //txt_test.setText(test.toString());

                    }

                }catch (Exception e){

                    Log.d("onResponse", "Error");

                    e.printStackTrace();

                }

            }



            @Override

            public void onFailure(Call<List<OrderItem>> call, Throwable t) {

            }

        });

    }*/

    class BackgroundTask extends AsyncTask<Void,Void,String> {

        String target;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target = "https://211.229.250.40:8000/";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();

                //한줄씩 읽어서 stringBuilder에 저장함
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(getActivity(), ManagementActivity.class);
            intent.putExtra("userList", result);//파싱한 값을 넘겨줌
            MainActivity.this.startActivity(intent);//ManagementActivity로 넘어감

        }

    }




        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }
    }
}
