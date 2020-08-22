package com.example.alifdeltaforce.realtimebooking;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alifdeltaforce.realtimebooking.InternetConnection.InternetConnect;
import com.example.alifdeltaforce.realtimebooking.Model.Session;
import com.example.alifdeltaforce.realtimebooking.Model.TripsInfo;
import com.example.alifdeltaforce.realtimebooking.Server.Server;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback {

    final private int REQUEST_LOCATION = 123;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = MapFragment.class.getSimpleName();
    private TextView txt_lat;
    private GoogleMap mgoogleMap;
    private Marker marker;
    double lat, lng;
    private Server server;

    private ProgressDialog progressDialog;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        server = InternetConnect.ApiClient().create(Server.class);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        txt_lat = (TextView) view.findViewById(R.id.mao_id_txt);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    LocationAvailability locationAvailability;
    LocationRequest locationRequest;

    private void CheckPermission_CallLocationProvider() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            locationRequest = LocationRequest.create()
                    .setFastestInterval(600000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }else {
            AlertDialog();
            Snackbar.make(getView(),"Location Provider is disable please check your Location and internet",Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        //การเรียกใช้ Location Service นั้นจะต้องเรียกหัลงจากที่ GoogleApiClient เรียกใช้  onConnected แล้วเท่านั้น
        CheckPermission_CallLocationProvider();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        ShowDialog_ConnectServer();
        txt_lat.setText(" Lat : "+location.getLatitude()+" Lng : "+location.getLongitude());
        MackerOnMap(location);
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    private void MackerOnMap(Location location){
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        marker = mgoogleMap.addMarker(new MarkerOptions().position(latLng).title("I am here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));
        GetAllTrips();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;

        mgoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
                Log.d(TAG,""+marker.getId()+""+marker.getTitle()+""+marker.getPosition());
//                        Bundle bundle=new Bundle();
//                        bundle.putString(Session.TAG_MARKER_INTENT,marker.getTitle());
//                        bundle.putDouble(Session.TAG_MY_CURRENT_LAT,lat);
//                        bundle.putDouble(Session.TAG_MY_CURRENT_LNG,lng);
//                        DirectionFragment directionFragment = new DirectionFragment();
//                        directionFragment.setArguments(bundle);
//                        fragmentTransaction.replace(R.id.content_frame, directionFragment).commit();
                Intent i = new Intent(getContext(),SelectActivity.class);
                i.putExtra(Session.TAG_MARKER_INTENT,marker.getTitle());
                i.putExtra(Session.TAG_MY_CURRENT_LAT,lat);
                i.putExtra(Session.TAG_MY_CURRENT_LNG,lng);
                startActivity(i);
                return true;
            }
        });
    }

    //Alert Dialog for Enable Location Provider
    private void AlertDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_alert_povider);
        dialog.setCancelable(true);

        TextView tv_Alert_title = (TextView) dialog.findViewById(R.id.tvAlert);
        TextView tv_Alert_detail = (TextView) dialog.findViewById(R.id.tvTitleAlert);
        TextView tv_Alert_details = (TextView) dialog.findViewById(R.id.tvTitleAlert2);
        Button btn_Alert_ok = (Button) dialog.findViewById(R.id.button_enable);
        Button btn_Alert_cancel = (Button) dialog.findViewById(R.id.button_cancel);

        tv_Alert_title.setText("Alert !!");
        tv_Alert_detail.setText("Please enable Location and setting");
        tv_Alert_details.setText("High Location and enable Internet");

        btn_Alert_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_Alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void GetAllTrips(){

        Call<TripsInfo> callTraps = server.ShowTripInfo();
        callTraps.enqueue(new Callback<TripsInfo>() {
            @Override
            public void onResponse(Call<TripsInfo> call, Response<TripsInfo> response) {
                Double van_lat,van_lng;
//                Snackbar.make(getView(),""+response.body().getMassage(),Snackbar.LENGTH_SHORT).show();

                if (marker != null){
                    marker.remove();
                }
                for (int i=0;i<response.body().getTrip().size();i++){
                    Log.d(TAG,""+response.body().getTrip().get(i).getLocation_of_bus_lah());
                    van_lat = Double.valueOf(response.body().getTrip().get(i).getLocation_of_bus_lah());
                    van_lng = Double.valueOf(response.body().getTrip().get(i).getLocation_of_bus_long());
                    LatLng location = new LatLng(van_lat,van_lng);
                    marker = mgoogleMap.addMarker(new MarkerOptions().position(location).title(""+response.body().getTrip().get(i).getBus_id()).snippet(" "+response.body().getTrip().get(i).getName_trips()));
                }

                progressDialog.dismiss();


                if (autoCheckLocation != null){
                    autoCheckLocation.interrupt();
                    Log.d("TestThread", "autoCheckLocation != null");
                }
                autoCheckLocation = new AutoCheckLocation();
                autoCheckLocation.start();

            }

            @Override
            public void onFailure(Call<TripsInfo> call, Throwable t) {
                Log.d(TAG,"Failure");
                progressDialog.dismiss();
            }
        });
    }



    private void ShowDialog_ConnectServer(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Find Current Location");
        progressDialog.setMessage("Please wait Finding....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }

    private  AutoCheckLocation autoCheckLocation;

    class AutoCheckLocation extends Thread{
        @Override
        public void run() {
            super.run();

            for (int i=1;i>0;i++) {
                try {
                    Thread.sleep(10000);
                    Log.d("TestThread", "CH 01 Map");
                    GetAllTrips();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("TestThread", "Map 01 Inter");
                    return;

                }
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
       if (autoCheckLocation != null){
           autoCheckLocation.interrupt();
//           Toast.makeText(getActivity(), "onDestroy() TREAD", Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (autoCheckLocation != null){
            autoCheckLocation.interrupt();
//            Toast.makeText(getActivity(), "onPause() TREAD", Toast.LENGTH_SHORT).show();
        }
    }
}
