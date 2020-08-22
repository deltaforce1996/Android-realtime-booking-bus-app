package com.example.alifdeltaforce.realtimebooking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.alifdeltaforce.realtimebooking.InternetConnection.InternetConnect;
import com.example.alifdeltaforce.realtimebooking.Model.BookingCheck;
import com.example.alifdeltaforce.realtimebooking.Model.Passenger;
import com.example.alifdeltaforce.realtimebooking.Model.Session;
import com.example.alifdeltaforce.realtimebooking.Model.TripsInfo;
import com.example.alifdeltaforce.realtimebooking.Server.Server;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionCallback, View.OnClickListener {


    private final String TAG = "SelectActivity";
    private LinearLayout linearLayout;
    private ProgressDialog progressDialog;
    private GoogleMap googleMap;
    private Toolbar mtoolbar;
    private CircleButton circleButton;

    private LatLng location_passenger;

    private LatLng origin;
    private LatLng destination;

    private String tripsID = "",tripsName,tripsSeat,p_name,p_phone;
    private Double curent_lat,current_lng;
    private Integer seat_number;

    private Server mserver;

    private String passengerdistance;
    private final Integer price = 300 ;

    private PolylineOptions polylineOptions;
    private String sessionPhone = "";
    private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        linearLayout = (LinearLayout) findViewById(R.id.container_layout);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar2);
        mtoolbar.setTitle("Trip Info");
        mtoolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        circleButton = (CircleButton) findViewById(R.id.btn_add);

        circleButton.setOnClickListener(this);

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectActivity.this,DrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mserver = InternetConnect.ApiClient().create(Server.class);

        Bundle bundle = getIntent().getExtras();
        tripsID =  bundle.getString(Session.TAG_MARKER_INTENT);
        curent_lat = bundle.getDouble(Session.TAG_MY_CURRENT_LAT);
        current_lng = bundle.getDouble(Session.TAG_MY_CURRENT_LNG);

        location_passenger = new LatLng(curent_lat,current_lng);

        Log.d(TAG,""+tripsID+""+current_lng+""+curent_lat);

        if(tripsID != ""&&curent_lat!=0.0&&current_lng!=0.0){
            GetTripsInfo(tripsID);
            preferences = getSharedPreferences(Session.TAG_FILE_SESSION, Context.MODE_PRIVATE);
            sessionPhone = preferences.getString(Session.TAG_SESSION,"");
            GetPassengerDetail();
            CheckStatus();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void Direction(){
        GoogleDirection.withServerKey(Session.SERVER_KEY_API)
                .from(origin)
                .to(destination)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .unit(Unit.METRIC)
                .transitMode(TransitMode.BUS)
                .execute(this);
    }

    private void ShowDialog_ConnectServer(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Connect Google Server");
        progressDialog.setMessage("Please wait Finding Direction....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void ShowDialog_Ch(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Connect");
        progressDialog.setMessage("Please wait checking....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        String status = direction.getStatus();
        Log.d(TAG,"direction :"+direction.getStatus());
        if (status.equals(RequestResult.OK)){
            googleMap.addMarker(new MarkerOptions().position(origin).title("Ven"));
            googleMap.addMarker(new MarkerOptions().position(destination).title("Destination"));

            Route route = direction.getRouteList().get(0);
            Leg leg = route.getLegList().get(0);
            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
            DrawDistance(directionPositionList,0);
            DirectionFromPassenger();

            googleMap.addPolyline(polylineOptions);
            setCameraWithCoordinationBounds(route);

        }else if (status.equals(RequestResult.REQUEST_DENIED)){
            Snackbar.make(linearLayout,"REQUEST_DENIED" ,Snackbar.LENGTH_SHORT).show();
        }else if (status.equals(RequestResult.NOT_FOUND)){
            Snackbar.make(linearLayout,"NOT_FOUND" ,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(linearLayout,"Check your internet",Snackbar.LENGTH_SHORT).show();

    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


    private void Show_PopUP(){

        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        TextView sh_name_trip = (TextView) bottomSheetView.findViewById(R.id.from_ID);
        TextView sh_price_trip = (TextView) bottomSheetView.findViewById(R.id.price_ID);
        TextView sh_distance_trip = (TextView) bottomSheetView.findViewById(R.id.distance_ID);
        TextView sh_seat_trip = (TextView) bottomSheetView.findViewById(R.id.num_seat_id);

        sh_name_trip.setText("Trip Name : "+tripsName);
        sh_seat_trip.setText("Number of seat : "+tripsSeat);
        sh_distance_trip.setText("Distance : \t"+passengerdistance);
        sh_price_trip.setText("Price : \t"+price+" Bath");

        Button btn_close = (Button) bottomSheetView.findViewById(R.id.button_close_trips);
        Button btn_select_trip = (Button) bottomSheetView.findViewById(R.id.button_select_trips);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.hide();
            }
        });
        btn_select_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog


                if (status==0){
                    Toast.makeText(SelectActivity.this, "Please wait response for driver ", Toast.LENGTH_SHORT).show();
                }else {
                bottomSheetDialog.hide();
                AlertDialog(tripsName,tripsID,passengerdistance,p_name,p_phone,curent_lat,current_lng);
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void GetTripsInfo(String tripsID){
        ShowDialog_ConnectServer();
        Call<TripsInfo> call = mserver.SelectTripInfo(tripsID);
        call.enqueue(new Callback<TripsInfo>() {
            @Override
            public void onResponse(Call<TripsInfo> call, Response<TripsInfo> response) {
                Snackbar.make(linearLayout,""+response.body().getMassage(),Snackbar.LENGTH_SHORT).show();
//                อย่าลืมดัก ERROR ในกรณีผู้โดยสารเลือก Marker ของผู้โดยสารเอง
                Double end_lat = Double.valueOf(response.body().getTrip().get(0).getEnd_lah());
                Double end_lng = Double.valueOf(response.body().getTrip().get(0).getEnd_long());
                Double bus_lat = Double.valueOf(response.body().getTrip().get(0).getLocation_of_bus_lah());
                Double bus_lng = Double.valueOf(response.body().getTrip().get(0).getLocation_of_bus_long());
                destination = new LatLng(end_lat,end_lng);
                origin = new LatLng(bus_lat,bus_lng);

                tripsName = response.body().getTrip().get(0).getName_trips();
                tripsSeat = response.body().getTrip().get(0).getBus_seat_info();

                Direction();
            }

            @Override
            public void onFailure(Call<TripsInfo> call, Throwable t) {
                Snackbar.make(linearLayout,"Check your internet",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void DirectionFromPassenger(){
        GoogleDirection.withServerKey(Session.SERVER_KEY_API)
                .from(location_passenger)
                .to(origin)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .unit(Unit.METRIC)
                .transitMode(TransitMode.BUS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        String status = direction.getStatus();
                        Log.d(TAG,"direction :"+direction.getStatus());

                        if (status.equals(RequestResult.OK)){

                            googleMap.addMarker(new MarkerOptions().position(location_passenger).title("Passenger").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPosition = leg.getDirectionPoint();
                            DrawDistance(directionPosition,1);

                            Info distanceInfo = leg.getDistance();
                            passengerdistance = distanceInfo.getText();
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Snackbar.make(linearLayout,"Check your internet",Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void DrawDistance(ArrayList drawdirectionPosition,int value){
        switch (value){
            case 0:
                polylineOptions = DirectionConverter.createPolyline(SelectActivity.this, drawdirectionPosition, 5, Color.RED);
                break;
            case 1:
                 polylineOptions = DirectionConverter.createPolyline(SelectActivity.this, drawdirectionPosition, 3, Color.BLUE);
                 break;
        }
        googleMap.addPolyline(polylineOptions);
    }

    @Override
    public void onClick(View v) {
        if (v==circleButton){
            Show_PopUP();
        }
    }

    private void AlertDialog(final String tripsName, final String tripsID, final String passengerdistance, final String p_name, final String p_phone, final Double curent_lat, final Double current_lng){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking);
        dialog.setCancelable(true);
       final EditText edt_seat = (EditText) dialog.findViewById(R.id.editText_seat);
       final Button btn_send_amount_seat = (Button) dialog.findViewById(R.id.button_send_seat);


        btn_send_amount_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectActivity.this,CheckBookActivity.class);

                String seat_num = edt_seat.getText().toString();

                if (!seat_num.isEmpty()){
                    seat_number = Integer.valueOf(seat_num);
                    i.putExtra(Session.TAG_SEAT,seat_number);
                    i.putExtra(Session.TAG_TRIPNAME,tripsName);
                    i.putExtra(Session.TAG_TRIPID,tripsID);
                    i.putExtra(Session.TAG_PASSENGERDISTANCE,passengerdistance);
                    i.putExtra(Session.TAG_PASSENGERNAME,p_name);
                    i.putExtra(Session.TAG_PASSENGERPHONE,p_phone);
                    i.putExtra(Session.TAG_PASSENGERLAT,curent_lat);
                    i.putExtra(Session.TAG_PASSENGERLNG,current_lng);
                    startActivity(i);
                }else {
                    Toast.makeText(SelectActivity.this, "Input seat", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();
    }

    private void GetPassengerDetail(){
        Call<Passenger> call = mserver.PassengerDetail(sessionPhone);
        call.enqueue(new Callback<Passenger>() {
            @Override
            public void onResponse(Call<Passenger> call, Response<Passenger> response) {
                p_name = response.body().getPassengerDeltial().get(0).getName_Passnger();
                p_phone = response.body().getPassengerDeltial().get(0).getTel_of_Passnger();
            }

            @Override
            public void onFailure(Call<Passenger> call, Throwable t) {
                Log.d(TAG,"Read Failure");
            }
        });
    }

   private int status = 0;

//    ควรจะทำให้มันเช็คตั้งแต่ตอนจจองเพราะอาจจะเกิดการตอบกลับขณะเลือกทริปไปแล้ว
    private void CheckStatus(){

        Call<BookingCheck> call = mserver.Check_status(sessionPhone);
        call.enqueue(new Callback<BookingCheck>() {
            @Override
            public void onResponse(Call<BookingCheck> call, Response<BookingCheck> response) {
                if (response.body().getPassengerDeltial().get(0).getStatus()==null){

                    Log.d(TAG,"Status is  "+response.body().getPassengerDeltial().get(0).getStatus());
                    status = 0;

                }else{
                    status = Integer.valueOf(response.body().getPassengerDeltial().get(0).getStatus());
//                    Toast.makeText(SelectActivity.this, ""+response.body().getPassengerDeltial().get(0).getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingCheck> call, Throwable t) {
                Log.d(TAG,"Status is onFailure");
            }
        });

    }


}
