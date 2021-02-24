package com.example.market;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public final String DATA_STORE = "DATA_STORE";
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

        idText= findViewById(R.id.insert_id);
        passwordText = findViewById(R.id.insert_password);
        btn_login = findViewById(R.id.btn_login);
        checkBox = findViewById(R.id.autoLogin);

        if (!getPreferenceString(autoLoginId).equals("") && !getPreferenceString(autoLoginPw).equals("")) {
            checkBox.setChecked(true);
            System.out.println("dlrj>>>>>>>>>>>>>>"+autoLoginId);
            checkAutoLogin(getPreferenceString(autoLoginId));
        }


        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                volleyPost();

            }
        });
    }

    public void checkAutoLogin(String id){

        //Toast.makeText(this, id + "님 환영합니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    public void volleyPost(){

        String postUrl = "http://211.229.250.40/api_init_session"; // 서버 주소
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {

            String userID = idText.getText().toString();
            String userPassword = passwordText.getText().toString();

            postData.put("input_id",userID);
            postData.put("input_pw", userPassword);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String success = "200";
                String errorId = "300";
                String errorPw = "400";

                try {
                    if (response.getString("result").equals(success)){
                        String userID = idText.getText().toString();
                        String userPassword = passwordText.getText().toString();
                        System.out.println("토큰 >>>>>>>>>>>>>"+response.getString("access_token"));
                        setPreference(token,response.getString("access_token") );
                        System.out.println("저장된 토큰 >>>>>>>>>"+getPreferenceString(token));
                        if(checkBox.isChecked()) {
                            setPreference(autoLoginId, userID);
                            setPreference(autoLoginPw, userPassword);
                        } else {
                            setPreference(autoLoginId, "");
                            setPreference(autoLoginPw, "");
                        }
                        //setPreference("token", response.getString("access_tp"));
                        Toast.makeText(LoginActivity.this, userID + "님 환영합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);

                    } else if (response.getString("result").equals(errorId)){

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("알림")
                                .setMessage("아이디가 존재하지 않습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else if (response.getString("result").equals(errorPw)) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest); // 서버에 요청

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




}
