package com.example.market.OrderList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface OrderListInterface {

    @POST("api_create_order")
    Call<List<OrderItem>> getOrderList();

}