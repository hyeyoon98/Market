package com.example.market;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.market.APIInterface.RetrofitClient;
import com.example.market.DTO.LoginRequest;
import com.example.market.DTO.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private com.example.market.APIInterface.initMyApi initMyApi;

    public final String DATA_STORE = "DATA_STORE";
    private BackPressCloseHandler backPressCloseHandler;
    EditText idText,passwordText;
    Button btn_login;
    CheckBox checkBox;
    String autoLoginId = "autoLoginId";
    String autoLoginPw = "autoLoginPw";
    String token = "token";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backPressCloseHandler = new BackPressCloseHandler(this);

        idText= findViewById(R.id.insert_id);
        passwordText = findViewById(R.id.insert_password);
        btn_login = findViewById(R.id.btn_login);
        checkBox = findViewById(R.id.autoLogin);


        if (!getPreferenceString(autoLoginId).equals("") && !getPreferenceString(autoLoginPw).equals("")) {
            checkBox.setChecked(true);
            checkAutoLogin(getPreferenceString(autoLoginId));
        }


        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String id = idText.getText().toString();
                String pw = passwordText.getText().toString();
                hideKeyboard();

                if (id.trim().length() == 0 || pw.trim().length() == 0 || id == null || pw == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("알림")
                            .setMessage("로그인 정보를 입력바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    //volleyPost();
                    LoginResponse();
                }
            }
        });
    }

    public void LoginResponse() {
        String userID = idText.getText().toString().trim();
        String userPassword = passwordText.getText().toString().trim();

        LoginRequest loginRequest = new LoginRequest(userID, userPassword);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        initMyApi.getLoginResponse(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Log.d("retrofit", "Data fetch success");

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse result = response.body();

                    String resultCode = result.getResultCode();
                    String token = result.getToken();

                    String success = "200";
                    String errorId = "300";
                    String errorPw = "400";


                    if (resultCode.equals(success)) {
                        String userID = idText.getText().toString();
                        String userPassword = passwordText.getText().toString();

                        setPreference(token,token);
                        System.out.println("저장된 토큰 >>>>>>>>>" + getPreferenceString(token));
                        if (checkBox.isChecked()) {
                            setPreference(autoLoginId, userID);
                            setPreference(autoLoginPw, userPassword);
                        } else {
                            setPreference(autoLoginId, "");
                            setPreference(autoLoginPw, "");
                        }

                        //passData();

                        //setPreference("token", response.getString("access_tp"));
                        Toast.makeText(LoginActivity.this, userID + "님 환영합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userId", userID);
                        startActivity(intent);
                        LoginActivity.this.finish();

                    } else if (resultCode.equals(errorId)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("알림")
                                .setMessage("아이디가 존재하지 않습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else if (resultCode.equals(errorPw)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("알림")
                                .setMessage("비밀번호가 일치하지 않습니다.\n 고객" +
                                        "센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();

            }
        });
    }

    public void setPreference(String key, String value){
        SharedPreferences pref = getSharedPreferences(DATA_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences(DATA_STORE, MODE_PRIVATE);
        return pref.getString(key, "");
    }


    //키보드 숨기기
    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(idText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordText.getWindowToken(), 0);
    }

    //화면 터치 시 키보드 내려감
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //자동 로그인
    public void checkAutoLogin(String id){

        //Toast.makeText(this, id + "님 환영합니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}
