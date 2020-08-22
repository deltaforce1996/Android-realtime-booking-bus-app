package com.example.alifdeltaforce.realtimebooking;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.alifdeltaforce.realtimebooking.InternetConnection.InternetConnect;
import com.example.alifdeltaforce.realtimebooking.Model.Passenger;
import com.example.alifdeltaforce.realtimebooking.Model.Session;
import com.example.alifdeltaforce.realtimebooking.Server.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserLoginFragment extends Fragment {

    private Button btn_login;
    private EditText editText_phone,editText_pass;
    private Server mserver;
    private String sessionPhone = "";
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;

    public UserLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_login = (Button) view.findViewById(R.id.button_login);
        editText_phone = (EditText) view.findViewById(R.id.edt_txt_phone);
        editText_pass = (EditText) view.findViewById(R.id.edt_txt_pass);

         preferences = getActivity().getSharedPreferences(Session.TAG_FILE_SESSION, Context.MODE_PRIVATE);

         sessionPhone = preferences.getString(Session.TAG_SESSION,"");

        if (sessionPhone != "") {
            Intent intent = new Intent(getActivity(),DrawerActivity.class);
            startActivity(intent);
            Snackbar.make(getView(),""+sessionPhone, Snackbar.LENGTH_SHORT).show();
        }

        mserver = InternetConnect.ApiClient().create(Server.class);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login(){

        String phone_num = editText_phone.getText().toString();
        String password = editText_pass.getText().toString();

        if (!phone_num.isEmpty()&&!password.isEmpty()){

            Call<Passenger> callLogind = mserver.PassengerLogin(phone_num,password);
            callLogind.enqueue(new Callback<Passenger>() {
                @Override
                public void onResponse(Call<Passenger> call, Response<Passenger> response) {
                    try {

                        edit = preferences.edit();
                        edit.putString(Session.TAG_SESSION,response.body().getPassengerDeltial().get(0).getTel_of_Passnger());
                        edit.commit();

                        Intent intent = new Intent(getActivity(),DrawerActivity.class);
                        startActivity(intent);

                        Snackbar.make(getView(),""+response.body().getMassage()+" "+response.body().getPassengerDeltial().get(0).getTel_of_Passnger(),Snackbar.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Snackbar.make(getView(),""+response.body().getMassage(),Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Passenger> call, Throwable t) {
                    Snackbar.make(getView(),"Failure Please check internet",Snackbar.LENGTH_SHORT).show();
                }
            });

        }else {
           Snackbar.make(getView(),"Some value is empty",Snackbar.LENGTH_SHORT).show();
        }
    }
}
