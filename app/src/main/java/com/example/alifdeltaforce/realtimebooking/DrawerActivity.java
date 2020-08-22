package com.example.alifdeltaforce.realtimebooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alifdeltaforce.realtimebooking.Model.Session;

public class DrawerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;

    private final String TAG="DrawerActivity";

    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = (Toolbar) findViewById(R.id.too_id);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);


        try {
            Bundle bundle = getIntent().getExtras();
            i = bundle.getInt(Session.TAG_SENDTODRAW);

            if (i==1){
                FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
                setTitle("Report");
                OderFragment oderFragment = new OderFragment();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_enter,R.anim.slide_left_exit);
                fragmentTransaction.replace(R.id.content_frame,oderFragment);
                fragmentTransaction.commit();
            }
        }catch (Exception e){
            Log.d(TAG," i = bundle.getInt(Session.TAG_SENDTODRAW) == i is null");
            setTitle("Map");
            FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
            MapFragment mapFragment = new MapFragment();
            fragmentTransaction.replace(R.id.content_frame,mapFragment);
            fragmentTransaction.commit();
        }


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(Session.TAG_FILE_SESSION, Context.MODE_PRIVATE);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(DrawerActivity.this,drawerLayout,R.string.drawerOpen,R.string.drawerClose);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.btn_nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()){
                    case R.id.btn_nav_map :
                        setTitle("Map");
                        MapFragment mapFragment = new MapFragment();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit);
                        fragmentTransaction.replace(R.id.content_frame,mapFragment);
                        fragmentTransaction.commit();
//                        Snackbar.make(drawerLayout, "Nav_Map", Snackbar.LENGTH_SHORT).show();
                        return true;
//                    case R.id.btn_nav_Status :
//                        setTitle("Status");
//                        StatusFragment statusFragment = new StatusFragment();
//                        fragmentTransaction.setCustomAnimations(R.anim.slide_left_enter,R.anim.slide_left_exit);
//                        fragmentTransaction.replace(R.id.content_frame,statusFragment);
//                        fragmentTransaction.commit();
////                        Snackbar.make(drawerLayout, "nav_Status", Snackbar.LENGTH_SHORT).show();
//                        return true;
                    case R.id.btn_nav_Oder :
                        setTitle("Report");
                        OderFragment oderFragment = new OderFragment();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_left_enter,R.anim.slide_left_exit);
                        fragmentTransaction.replace(R.id.content_frame,oderFragment);
                        fragmentTransaction.commit();
//                        Snackbar.make(drawerLayout, "nav_Oder", Snackbar.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_id);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();

                        switch (item.getItemId()){
                            case R.id.nav_map :
                                setTitle("Map");
                                MapFragment mapFragment = new MapFragment();
                                fragmentTransaction.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit);
                                fragmentTransaction.replace(R.id.content_frame,mapFragment);
                                fragmentTransaction.commit();
//                                Snackbar.make(drawerLayout, "Map", Snackbar.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_Profile :
                                setTitle("Profile");
                                ProfileFragment profileFragment = new ProfileFragment();
                                fragmentTransaction.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit);
                                fragmentTransaction.replace(R.id.content_frame,profileFragment);
                                fragmentTransaction.commit();
//                                Snackbar.make(drawerLayout, "Profile", Snackbar.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_Oder :
                                setTitle("History");
                                HistoryFragment historyFragment = new HistoryFragment();
                                fragmentTransaction.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit);
                                fragmentTransaction.replace(R.id.content_frame,historyFragment);
                                fragmentTransaction.commit();
//                                Snackbar.make(drawerLayout, "History", Snackbar.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_logout :
                                edit = preferences.edit();
                                edit.remove(Session.TAG_SESSION);
                                edit.commit();
                                Intent intent = new Intent(DrawerActivity.this,MainActivity.class);
                                startActivity(intent);
                                Snackbar.make(drawerLayout, "Log Out", Snackbar.LENGTH_SHORT).show();
                                break;

                        }

                        // set item as selected to persist highlight
                        item.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        return true;
                    }
                }
        );

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
