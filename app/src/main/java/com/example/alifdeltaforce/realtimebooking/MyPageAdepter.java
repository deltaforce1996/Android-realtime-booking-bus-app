package com.example.alifdeltaforce.realtimebooking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Alif Delta Force on 7/8/2018.
 */

public class MyPageAdepter extends FragmentPagerAdapter {

    private static final int NUM_PAGE = 2;
    private static final String CURRENT_STEP_POSITION_KEY = "CURRENT_STEP_POSITION_KEY";

    public MyPageAdepter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                final UserLoginFragment step1 = new UserLoginFragment();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
                step1.setArguments(b1);
                return step1;

            case 1:
                final UserRegisterFragment step2 = new UserRegisterFragment();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;
        }

        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGE;
    }
}
