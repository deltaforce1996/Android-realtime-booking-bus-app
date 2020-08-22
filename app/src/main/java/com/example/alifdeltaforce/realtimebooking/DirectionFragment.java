package com.example.alifdeltaforce.realtimebooking;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.alifdeltaforce.realtimebooking.InternetConnection.InternetConnect;
import com.example.alifdeltaforce.realtimebooking.Model.Session;
import com.example.alifdeltaforce.realtimebooking.Model.TripsInfo;
import com.example.alifdeltaforce.realtimebooking.Server.Server;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DirectionFragment extends Fragment implements OnMapReadyCallback, DirectionCallback {

    private final String TAG = "DirectionFragment";
    private LinearLayout linearLayout;
    private ProgressDialog progressDialog;
    private GoogleMap googleMap;

    private LatLng origin;
    private LatLng destination;

    private String tripsID = "";
    private Double curent_lat,current_lng;

    private Server mserver;

    private String distance;
    private final Integer price = 300 ;



    public DirectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_direction, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayout = (LinearLayout) view.findViewById(R.id.linear_Dirction);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mserver = InternetConnect.ApiClient().create(Server.class);

        tripsID =  getArguments().getString(Session.TAG_MARKER_INTENT);
        curent_lat = getArguments().getDouble(Session.TAG_MY_CURRENT_LAT);
        current_lng = getArguments().getDouble(Session.TAG_MY_CURRENT_LNG);

        Log.d(TAG,""+tripsID+""+current_lng+""+curent_lat);

        //อาจจะใส่ Bottom ลงไป
        if(tripsID != ""&&curent_lat!=0.0&&current_lng!=0.0){
            GetTripsInfo(tripsID);
        }
    }
// Request แบบ GET โดยการส่ง API key ส่งละติจูดลอติจูด ผ่านไลบารี่ที่มรชื่อว่า akexorcist.googledirection ไปยัง Web service ของ google map
    private void Direction(){
        ShowDialog_ConnectServer();
        GoogleDirection.withServerKey(Session.SERVER_KEY_API)
                .from(origin)
                .to(destination)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .unit(Unit.METRIC)
                .transitMode(TransitMode.BUS)
                .execute(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

// จะได้ Response เป็น json นำข้อมูลที่ได้มาลากเส้นการฟิกทับไปบน Map โดยเียกใช้ class PolylineOptions
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        String status = direction.getStatus();
        progressDialog.dismiss();
        Log.d(TAG,"direction :"+direction.getStatus());

        if (status.equals(RequestResult.OK)){

            googleMap.addMarker(new MarkerOptions().position(origin).title("Ven"));
            googleMap.addMarker(new MarkerOptions().position(destination).title("Taraget"));

            Route route = direction.getRouteList().get(0);
            Leg leg = route.getLegList().get(0);
            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getActivity(), directionPositionList, 3, Color.RED);

            Info distanceInfo = leg.getDistance();
            distance = distanceInfo.getText();

            Snackbar.make(linearLayout,"OK : "+distance ,Snackbar.LENGTH_SHORT).show();

            googleMap.addPolyline(polylineOptions);
            setCameraWithCoordinationBounds(route);
//            Show_PopUP(distance);

        }else if (status.equals(RequestResult.REQUEST_DENIED)){

            Snackbar.make(linearLayout,"REQUEST_DENIED" ,Snackbar.LENGTH_SHORT).show();

        }else if (status.equals(RequestResult.NOT_FOUND)){

            Snackbar.make(linearLayout,"NOT_FOUND" ,Snackbar.LENGTH_SHORT).show();

        }else if (status.equals(RequestResult.UNKNOWN_ERROR)){

            Snackbar.make(linearLayout,"UNKNOWN_ERROR" ,Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(linearLayout,"onDirectionFailure" ,Snackbar.LENGTH_SHORT).show();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    private void ShowDialog_ConnectServer(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Connect Google Server");
        progressDialog.setMessage("Please wait Finding Direction....");
        progressDialog.show();
    }


    private void Show_PopUP(String distance){

       View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
       final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
       bottomSheetDialog.setContentView(bottomSheetView);
       BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        TextView sh_name_trip = (TextView) bottomSheetView.findViewById(R.id.from_ID);
        TextView sh_price_trip = (TextView) bottomSheetView.findViewById(R.id.price_ID);
        TextView sh_distance_trip = (TextView) bottomSheetView.findViewById(R.id.distance_ID);
        TextView sh_seat_trip = (TextView) bottomSheetView.findViewById(R.id.num_seat_id);

        sh_distance_trip.setText("Distance : \t"+distance);
        sh_price_trip.setText("Price : \t"+price+" Bath");

        Button btn_close = (Button) bottomSheetView.findViewById(R.id.button_close_trips);
        Button btn_select_trip = (Button) bottomSheetView.findViewById(R.id.button_select_trips);

        bottomSheetDialog.show();
    }


   private void GetTripsInfo(String tripsID){
       Call<TripsInfo> call = mserver.SelectTripInfo(tripsID);
       call.enqueue(new Callback<TripsInfo>() {
           @Override
           public void onResponse(Call<TripsInfo> call, Response<TripsInfo> response) {

               Snackbar.make(getView(),""+response.body().getMassage(),Snackbar.LENGTH_SHORT).show();

              Double end_lat = Double.valueOf(response.body().getTrip().get(0).getEnd_lah());
              Double end_lng = Double.valueOf(response.body().getTrip().get(0).getEnd_long());
              origin = new LatLng(end_lat,end_lng);
              Double bus_lat = Double.valueOf(response.body().getTrip().get(0).getLocation_of_bus_lah());
              Double bus_lng = Double.valueOf(response.body().getTrip().get(0).getLocation_of_bus_long());
              destination = new LatLng(bus_lat,bus_lng);
              Direction();
           }

           @Override
           public void onFailure(Call<TripsInfo> call, Throwable t) {
               Snackbar.make(getView(),"Check your internet",Snackbar.LENGTH_SHORT).show();
           }
       });
   }
}
