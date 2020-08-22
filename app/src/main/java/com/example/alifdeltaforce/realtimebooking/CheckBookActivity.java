package com.example.alifdeltaforce.realtimebooking;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alifdeltaforce.realtimebooking.InternetConnection.InternetConnect;
import com.example.alifdeltaforce.realtimebooking.Model.Booking;
import com.example.alifdeltaforce.realtimebooking.Model.Session;
import com.example.alifdeltaforce.realtimebooking.Server.Server;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckBookActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private EditText edt_name,edt_phone,edt_car,edt_bookID;
    private Integer number_Seat;
    private String name,phone,nametrip,book_ID,distance,tripsID,curent_lat,current_lng;
    private TextView text_seat,text_trips,text_total,text_distance;
    private Double pd_lat,pd_lng;
    private Server server;
    private LinearLayout linearLayout;
    private Button btn_save;

    private final String TAG = "CheckBookActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_book);

        server = InternetConnect.ApiClient().create(Server.class);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar_check_id);
        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setTitle("Check Data");
        setSupportActionBar(mtoolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edt_name = (EditText) findViewById(R.id.edit_name);
        edt_phone = (EditText) findViewById(R.id.edit_phone);
        edt_car = (EditText) findViewById(R.id.edit_car);
        edt_bookID = (EditText) findViewById(R.id.booking_ID);

        text_seat = (TextView) findViewById(R.id.txt_amount);
        text_distance = (TextView) findViewById(R.id.txt_passengerDis);
        text_total = (TextView) findViewById(R.id.txt_totle);
        text_trips = (TextView) findViewById(R.id.trip_name);

        btn_save = (Button) findViewById(R.id.button_confirm);

        linearLayout = (LinearLayout) findViewById(R.id.id_learL);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveBookingTrips();
            }
        });

//        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CheckBookActivity.this,SelectActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        GetDataForCheck();

    }

    private void GetDataForCheck(){

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString(Session.TAG_PASSENGERNAME);
        phone = bundle.getString(Session.TAG_PASSENGERPHONE);
        number_Seat = bundle.getInt(Session.TAG_SEAT);
        tripsID = bundle.getString(Session.TAG_TRIPID);
        nametrip = bundle.getString(Session.TAG_TRIPNAME);
        distance = bundle.getString(Session.TAG_PASSENGERDISTANCE);
        pd_lat = bundle.getDouble(Session.TAG_PASSENGERLAT);
        pd_lng = bundle.getDouble(Session.TAG_PASSENGERLNG);

        book_ID = GenerateString(19);

        curent_lat = String.valueOf(pd_lat);
        current_lng = String.valueOf(pd_lng);

        edt_name.setText(name);
        edt_phone.setText(phone);
        edt_car.setText(tripsID);
        edt_bookID.setText(book_ID);

        text_seat.setText(""+number_Seat);
        text_distance.setText(distance);
        text_trips.setText(nametrip);

        Double total = number_Seat*300.0;

        text_total.setText(""+total+" Bath");
    }

    private String GenerateString(int lenght){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i=0;i<lenght;i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void SaveBookingTrips(){
        Call<Booking> call = server.SaveBooking(book_ID,tripsID,phone,number_Seat,current_lng,curent_lat);
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {

                if (response.body().getStatus()==1) {
                    Intent intent = new Intent(CheckBookActivity.this, DrawerActivity.class);
                    intent.putExtra(Session.TAG_SENDTODRAW, 1);
                    startActivity(intent);
                }
                Snackbar.make(linearLayout,""+response.body().getMassage(),Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Log.d(TAG,"Failure");
            }
        });
    }
}
