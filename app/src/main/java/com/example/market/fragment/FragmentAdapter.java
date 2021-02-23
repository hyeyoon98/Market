package com.example.market.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.market.OrderHistoryActivity;

public class FragmentAdapter extends FragmentPagerAdapter {

    public static int PAGE_POSITION = 3;

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return OrderFragment.newInstance();
            case 1:
                return OrderHistoryFragment.newInstance();
            case 2:
                return MyPageFragment.newInstance();
            default:

                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
