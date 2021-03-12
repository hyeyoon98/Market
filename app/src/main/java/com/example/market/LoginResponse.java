package com.example.market;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("result")
    public String resultCode;

    @SerializedName("access_token")
    public String token;

    @SerializedName("staff")
    public String staff;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }
}
