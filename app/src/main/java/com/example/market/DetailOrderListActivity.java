package com.example.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.market.R;

public class DetailOrderListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order_list);

        Intent intent = getIntent();

        TextView marketName = (TextView)findViewById(R.id.dialog_title),
                date = (TextView)findViewById(R.id.dialog_date),
                content = (TextView)findViewById(R.id.dialog_content);
        ConstraintLayout btnClose =(ConstraintLayout) findViewById(R.id.btn_close);

        String getMarketName = intent.getStringExtra("marketName");
        System.out.println("getMarketName>>>>>>>>>"+getMarketName);
        if (getMarketName != null){
            marketName.setText(getMarketName);
        }

        String getDate = intent.getStringExtra("date");
        System.out.println("getMarketName>>>>>>>>>"+getDate);
        if (getDate != null) {
            date.setText(getDate);
        }

        String getContent = intent.getStringExtra("content");
        System.out.println("getMarketName>>>>>>>>>"+getContent);
        if (getContent != null) {
            content.setText(getContent);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
