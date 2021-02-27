package com.example.market.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.market.LoginActivity;
import com.example.market.MainActivity;
import com.example.market.R;

import static android.content.Context.MODE_PRIVATE;

public class MyPageFragment extends Fragment  {

    public final String DATA_STORE = "DATA_STORE";
    private String profileID = "profile_id";
    String autoLoginId = "autoLoginId";
    String autoLoginPw = "autoLoginPw";


    public MyPageFragment(){}

    public static MyPageFragment newInstance() {
        MyPageFragment myPageFragment = new MyPageFragment();
        Bundle bundle = new Bundle();
        return myPageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        TextView tvUser = view.findViewById(R.id.id_user);
        String profileId = getPreferenceString(profileID);
        tvUser.setText(profileId+" 사장님\n번창하세요*^^*");





        //버튼 이벤트
        ConstraintLayout btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSharedPreferences(autoLoginId);
                removeSharedPreferences(autoLoginPw);
                ActivityCompat.finishAffinity(getActivity());

                /*//프래그먼트 종료
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .remove(MyPageFragment.this)
                        .commit();*/

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void removeSharedPreferences(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences(DATA_STORE, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.remove(key);
        edit.commit();
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getContext().getSharedPreferences(DATA_STORE, MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
