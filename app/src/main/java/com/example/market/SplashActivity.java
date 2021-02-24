package com.example.market;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView textImg = (ImageView)findViewById(R.id.img_logo);
        ImageView carImg = (ImageView)findViewById(R.id.img_car);


        Animation imageAnim = AnimationUtils.loadAnimation(this, R.anim.ani_scale);
        carImg.startAnimation(imageAnim);

        Animation textAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ani_translate);
        imageAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textImg.setVisibility(View.VISIBLE);
                textImg.startAnimation(textAnim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new splashHandler(), 3000);
    }

    private class splashHandler implements Runnable{

        @Override
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class));
            SplashActivity.this.finish();
        }
    }
}