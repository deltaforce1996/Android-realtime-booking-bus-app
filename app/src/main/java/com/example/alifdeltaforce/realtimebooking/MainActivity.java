package com.example.alifdeltaforce.realtimebooking;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import at.markushi.ui.CircleButton;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    private MyPageAdepter myPageAdepter;
    private CircleIndicator indicator;
    private CircleButton circleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_paper);
        indicator = (CircleIndicator) findViewById(R.id.indicator_id);
        circleButton = (CircleButton) findViewById(R.id.btn_next);

        myPageAdepter = new MyPageAdepter(getSupportFragmentManager());

        viewPager.setAdapter(myPageAdepter);
        indicator.setViewPager(viewPager);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem()){
                    case 0:
                        viewPager.setCurrentItem(getItem(+1),true);
                        break;
                    case 1:
                        viewPager.setCurrentItem(getItem(-1),true);
                        break;
                }

            }
        });
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}
