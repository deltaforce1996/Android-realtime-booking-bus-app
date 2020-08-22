package com.example.alifdeltaforce.realtimebooking;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.alifdeltaforce.realtimebooking.InternetConnection.InternetConnect;
import com.example.alifdeltaforce.realtimebooking.Model.Booking;
import com.example.alifdeltaforce.realtimebooking.Model.Session;
import com.example.alifdeltaforce.realtimebooking.Server.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar toolbar_pay;
    private String bookID;
    private Server server;
    private TextView t_line,t_busid,t_phone,t_seat,t_total;

    private final String TAG = "PaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        server = InternetConnect.ApiClient().create(Server.class);

        t_line = (TextView) findViewById(R.id.show_TripLine);
        t_busid = (TextView) findViewById(R.id.show_busid);
        t_phone = (TextView) findViewById(R.id.show_Phone);
        t_seat = (TextView) findViewById(R.id.show_Seat);
        t_total = (TextView) findViewById(R.id.show_tatal);

        toolbar_pay = (Toolbar) findViewById(R.id.toolbar_slep_id);
        toolbar_pay.setTitle("Payment");
        toolbar_pay.setTitleTextColor(Color.WHITE);


        bookID = getIntent().getStringExtra(Session.TAG_FORGENREPORT);
        if (bookID!=null){
            GetShowPayout();
        }
    }

    private void GetShowPayout(){
        Call<Booking> call = server.GetForPayout(bookID);
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {

                t_line.setText(""+response.body().getBooking().get(0).getTripsName());
                t_busid.setText(""+response.body().getBooking().get(0).getBus_id());
                t_phone.setText(""+response.body().getBooking().get(0).getTel_passenger());
                t_seat.setText(""+response.body().getBooking().get(0).getAmount_seat());

                Double y = Double.valueOf(response.body().getBooking().get(0).getAmount_seat());
                Double total = 300.0*y;

                t_total.setText(""+total);
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Log.d(TAG,"Failure");

            }
        });
    }
}
