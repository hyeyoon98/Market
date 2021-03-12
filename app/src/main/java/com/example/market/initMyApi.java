package com.example.market;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface initMyApi {

    @POST("/api_init_session")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

}
