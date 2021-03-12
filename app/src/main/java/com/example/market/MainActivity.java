package com.example.market;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.market.fragment.FragmentAdapter;
import com.example.market.fragment.MyPageFragment;
import com.example.market.fragment.OrderFragment;
import com.example.market.fragment.OrderHistoryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.market.fragment.FragmentAdapter.PAGE_POSITION;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = OrderFragment.class.getSimpleName();
    private File tempFile;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 0;
    Bitmap originalBm;

    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1000;
    FloatingActionButton fab;
    private TabLayout tabLayout;
    private ArrayList <String> tabNames = new ArrayList<>();
    private BackPressCloseHandler backPressCloseHandler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        MyPageFragment myPageFragment = new MyPageFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.viewPager,myPageFragment)
                .commit();

        System.out.println("메인값 >>>>>>>>>"+userId);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        myPageFragment.setArguments(bundle);


        //Tab 설정
        loadTabName();
        setTabLayout();
        setViewPager();
        setCallButton();

        //플로팅 버튼 (권한 설정)
        setCallButton();
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("앱 권한");
                    alertDialog.setMessage("해당 앱의 원활할 기능을 이용하시려면 애플리케이션 정보 > 권한에서 모든 권한을 허용해 주십시오.");
                    alertDialog.setPositiveButton("권한설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent2 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:"+getApplicationContext().getPackageName()));
                            startActivity(intent2);
                            dialog.cancel();
                        }
                    });
                    alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
                return;
       }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void setTabLayout(){
        tabLayout = findViewById(R.id.tab);

        tabNames.stream().forEach(name ->tabLayout.addTab(tabLayout.newTab().setText(name)));

    }

    //탭 이름 설정
    private void loadTabName(){
        tabNames.add("주문하기");
        tabNames.add("주문내역");
        tabNames.add("마이페이지");
    }

    //뷰페이져 설정
    private void setViewPager() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), PAGE_POSITION);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //플로팅 버튼 (권한 설정)
    private void setCallButton() {
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:15660041"));
                AlertDialog.Builder alertCall = new AlertDialog.Builder(MainActivity.this);
                alertCall.setTitle("고객센터");
                alertCall.setMessage("전화를 연결하시겠습니까?");
                alertCall.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                        }else {
                            try {
                                startActivity(intent);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        dialog.cancel();
                    }
                });
                alertCall.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertCall.show();

            }
        });
    }

    @Override public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}