package com.example.alifdeltaforce.realtimebooking;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alifdeltaforce.realtimebooking.Adepter.OderAdepter;
import com.example.alifdeltaforce.realtimebooking.InternetConnection.InternetConnect;
import com.example.alifdeltaforce.realtimebooking.Model.Booking;
import com.example.alifdeltaforce.realtimebooking.Model.BookingCheck;
import com.example.alifdeltaforce.realtimebooking.Model.Session;
import com.example.alifdeltaforce.realtimebooking.Server.Server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OderFragment extends Fragment {


    public OderFragment() {
        // Required empty public constructor
    }

    private SharedPreferences preferences;
    private String sessionPhone = "";
    private Server server;
    private final String TAG = "OderFragment";

    private List<Booking.BookingBean> data_booking;

    private RecyclerView recyclerView;
    private OderAdepter adepter;
    private RecyclerView.LayoutManager layoutManager;

    private AutoCheckStatus autoCheckStatus;

    private List<String> all_item_status = new ArrayList<>();

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_oder, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_report_view);

        server = InternetConnect.ApiClient().create(Server.class);

        preferences = getActivity().getSharedPreferences(Session.TAG_FILE_SESSION, Context.MODE_PRIVATE);
        sessionPhone = preferences.getString(Session.TAG_SESSION,"");

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        GetDataForSetList();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //การเรียกใช้งาน Handler().postDelayed(Runnable,time) คือการสั่งให้ Thread รอ 4 วินาทีถึงจะทำงาน
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        adepter.notifyDataSetChanged();
                        GetDataForSetList();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },4000);
            }
        });


    }

    private String array_size = "0";

    private void GetDataForSetList(){
        Call<Booking> call = server.GetMyTrip(sessionPhone);
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {

                data_booking = new ArrayList<>();
                Booking result = response.body();

                Log.d(TAG,""+response.body().getBooking());

                data_booking = result.getBooking();
                Log.d(TAG,""+data_booking);

                array_size = String.valueOf(data_booking.size());
                try {
                    if (all_item_status != null){
                        all_item_status.clear();
                    }
                    for (int i =0;i<data_booking.size();i++) {
                        all_item_status.add(response.body().getBooking().get(i).getStatus());
                    }
                    Log.d(TAG,"item_status not Exception e :"+all_item_status);

                    for (int i=0;i<all_item_status.size();i++){
                        if (all_item_status.get(i).equals("0")){
//                            autoCheckStatus = new AutoCheckStatus();
//                            autoCheckStatus.start();
                             Log.d(TAG,"equals 0 not Exception e :"+all_item_status.get(i));
                        }
                    }

                    Log.d(TAG,"equals 0 not Exception e :"+all_item_status);
                }catch (Exception e){
                    Log.d(TAG," item_status Exception e :"+data_booking.size());
                }

//                Toast.makeText(getContext(), "Array size is : "+data_booking.size(), Toast.LENGTH_SHORT).show();

                adepter = new OderAdepter(data_booking,getContext());

                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adepter);


            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Log.d(TAG,"onFailure");
            }
        });
    }

    private void CheckStatus(){

        Call<BookingCheck> call = server.Check_status(sessionPhone);
        call.enqueue(new Callback<BookingCheck>() {
            @Override
            public void onResponse(Call<BookingCheck> call, Response<BookingCheck> response) {
                try {
                    if (response.body().getPassengerDeltial().get(0).getStatus() == null) {
                        Log.d(TAG, "Status is null");
                    } else {
                        int status = Integer.valueOf(response.body().getPassengerDeltial().get(0).getStatus());
                        if (status == 0) {
                            // Counter Trips Time
                            Toast.makeText(getContext(), "Please WAIT", Toast.LENGTH_SHORT).show();
                        } else if (status == 1) {
                            // Nontification

//                            GetDataForSetList();
                            if (autoCheckStatus != null){
                                autoCheckStatus.interrupt();
                                Notification();
                            }
                        }
                    }
                }catch (Exception e) {

                    Log.d(TAG, "Response format not match");
                }
            }

            @Override
            public void onFailure(Call<BookingCheck> call, Throwable t) {
                Log.d(TAG,"onFailure");
            }
        });
    }

    private void Notification(){

        if (autoCheckStatus != null){
            autoCheckStatus.interrupt();
        }

        long[] pattern = {500,500,500,500,500,500,500,500,500};
        Notification notification =
                new NotificationCompat.Builder(getContext()) // this is context
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("DevAhoy News")
                        .setContentText("สวัสดีครับ ยินดีต้อนรับเข้าสู่บทความ Android Notification :)")
                        .setAutoCancel(true)
                        .setVibrate(pattern)
                        .build();

        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);


    }

    //การเช็คข้อมูล
    class AutoCheckStatus extends Thread{

        @Override
        public void run() {
            super.run();

            for (int i=1;i>0;i++) {
                try {
                    Thread.sleep(10000);
                    Log.d("TestThread", "CH 02 Oder");
                    CheckStatus();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("TestThread", "Oder 01 Inter");
                    return;
                }
            }

        }
    }


//    @Override
//    public void onPause() {
//        super.onPause();
//        if (autoCheckStatus != null){
//            autoCheckStatus.interrupt();
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (autoCheckStatus != null){
//        autoCheckStatus.interrupt();
//        }
//    }
}
