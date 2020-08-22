package com.example.alifdeltaforce.realtimebooking;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alifdeltaforce.realtimebooking.InternetConnection.InternetConnect;
import com.example.alifdeltaforce.realtimebooking.Model.Passenger;
import com.example.alifdeltaforce.realtimebooking.Model.Session;
import com.example.alifdeltaforce.realtimebooking.Server.Server;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private SharedPreferences preferences;
    private String sessionPhone;
    private TextView tv_name,tv_birth,tv_phone,tv_password;
    private Server mserver;
    private String name,birth,phone,pass,txt_img;
    private CircleImageView imageView;
    private Bitmap img_profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_name = (TextView) view.findViewById(R.id.edt_name);
        tv_birth = (TextView) view.findViewById(R.id.textView_birth);
        tv_phone = (TextView) view.findViewById(R.id.textView_phone);
        tv_password = (TextView) view.findViewById(R.id.textView_pass);
        imageView = (CircleImageView) view.findViewById(R.id.circleImageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 checkPermissionsAndOpenFilePicker();
            }
        });

        mserver = InternetConnect.ApiClient().create(Server.class);

        preferences = getActivity().getSharedPreferences(Session.TAG_FILE_SESSION,Context.MODE_PRIVATE);
        sessionPhone = preferences.getString(Session.TAG_SESSION,"");

        if (sessionPhone != ""){
            GetPassengerDetail();
        }
    }

    private void GetPassengerDetail(){
       Call<Passenger> call = mserver.PassengerDetail(sessionPhone);
       call.enqueue(new Callback<Passenger>() {
           @Override
           public void onResponse(Call<Passenger> call, Response<Passenger> response) {
               name = response.body().getPassengerDeltial().get(0).getName_Passnger();
               birth = response.body().getPassengerDeltial().get(0).getDate_of_birth();
               phone = response.body().getPassengerDeltial().get(0).getTel_of_Passnger();
               pass = response.body().getPassengerDeltial().get(0).getPassword_Passnger();
               txt_img = Session.PIC_URL+""+response.body().getPassengerDeltial().get(0).getPicture_Passnger();

//               if (response.body().getPassengerDeltial().get(0).getPicture_Passnger() != ""){
//                   GetImageFormUrl getImageFormUrl = new GetImageFormUrl();
//                   getImageFormUrl.start();
//               }

               tv_name.setText(name);
               tv_birth.setText(birth);
               tv_phone.setText(phone);
               tv_password.setText(pass);
           }

           @Override
           public void onFailure(Call<Passenger> call, Throwable t) {
//               Snackbar.make(getView(),"Failure",Snackbar.LENGTH_SHORT).show();
           }
       });

    }

    public static Bitmap getBitmapFromURL(String txt_url) {
        try {
            URL url = new URL(txt_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetImageFormUrl extends Thread{
        @Override
        public void run() {
            super.run();
            img_profile = getBitmapFromURL(txt_img);
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(img_profile);
                }
            });

        }
    }


//    private void checkPermissionsAndOpenFilePicker() {
//        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
//        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
//                showError();
//            } else {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, Session.PERMISSIONS_REQUEST_CODE);
//            }
//        } else {
//            openFilePicker();
//        }
//    }
//
//    private void showError() {
//       Snackbar.make(getView(), "Allow external storage reading", Snackbar.LENGTH_SHORT).show();
//    }
//
//    private void openFilePicker() {
//        new MaterialFilePicker()
//                .withSupportFragment(this)
//                .withRequestCode(Session.FILE_PICKER_REQUEST_CODE)
//                .withHiddenFiles(true)
//                .start();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == Session.FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//
//            if (path != null) {
//                Log.d("Path (fragment): ", path);
//                Toast.makeText(getContext(), "Picked file in fragment: " + path, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

}
