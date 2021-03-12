package com.example.market.APIInterface;

import com.example.market.DTO.LoginRequest;
import com.example.market.DTO.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface initMyApi {

    @POST("/api_init_session")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

}
