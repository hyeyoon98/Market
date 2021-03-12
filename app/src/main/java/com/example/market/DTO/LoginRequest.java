package com.example.market.DTO;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("input_id")
    public String inputId;

    @SerializedName("input_pw")
    public String inputPw;

    public String getInputId() {
        return inputId;
    }

    public String getInputPw() {
        return inputPw;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public void setInputPw(String inputPw) {
        this.inputPw = inputPw;
    }

    public LoginRequest(String inputId, String inputPw) {
        this.inputId = inputId;
        this.inputPw = inputPw;
    }
}
