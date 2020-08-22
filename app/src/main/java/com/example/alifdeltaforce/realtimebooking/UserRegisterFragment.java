package com.example.alifdeltaforce.realtimebooking;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.alifdeltaforce.realtimebooking.InternetConnection.InternetConnect;
import com.example.alifdeltaforce.realtimebooking.Model.Passenger;
import com.example.alifdeltaforce.realtimebooking.Server.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserRegisterFragment extends Fragment {

    private EditText editText_userName,editText_dateTime,editText_phonNum,editText_passWord,editText_conPass;
    private Button btn_register;
    private Server mserver;
    private String gender;

    private final String TAG = "UserRegisterFragment";

    public UserRegisterFragment() {
        // Required empty public constructor
    }

    private RadioGroup radioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_register, container, false);
        return v;
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText_userName = (EditText) view.findViewById(R.id.edt_txt_userName);
        editText_dateTime = (EditText) view.findViewById(R.id.edt_txt_dateTime);
        editText_phonNum = (EditText) view.findViewById(R.id.edt_txt_phoneNum);
        editText_passWord = (EditText) view.findViewById(R.id.edt_txt_passWord);
        editText_conPass = (EditText) view.findViewById(R.id.edt_txt_conPass);

        mserver = InternetConnect.ApiClient().create(Server.class);

        btn_register = (Button) view.findViewById(R.id.button_register);

        radioGroup = (RadioGroup) view.findViewById(R.id.group_radio_button);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_male:
                        gender = "Male";
                        Snackbar.make(getView(),""+gender, Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_female:
                        gender = "Female";
                        Snackbar.make(getView(),""+gender, Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    private void Register(){
        String userName = editText_userName.getText().toString();
        String dateTime = editText_dateTime.getText().toString();
        String phoneNum = editText_phonNum.getText().toString();
        String passWord = editText_passWord.getText().toString();
        String conPass = editText_conPass.getText().toString();

        if (!userName.isEmpty()&&!dateTime.isEmpty()&&!phoneNum.isEmpty()&&!passWord.isEmpty()&&!conPass.isEmpty()&&gender!=""){
//            Insert Code Register
            Call<Passenger> callServerRegister = mserver.PassengerRegister(userName,dateTime,phoneNum,passWord,gender);
            callServerRegister.enqueue(new Callback<Passenger>() {
                @Override
                public void onResponse(Call<Passenger> call, Response<Passenger> response) {
//                    try{
//                        Log.d(TAG,"Response not Exception");
//                    }catch (Exception e){
                        Snackbar.make(getView(),""+response.body().getMassage(),Snackbar.LENGTH_SHORT).show();
//                    }
                }

                @Override
                public void onFailure(Call<Passenger> call, Throwable t) {
                    Snackbar.make(getView(),"Failure Please check internet",Snackbar.LENGTH_SHORT).show();
                }
            });
        }else {
            Snackbar.make(getView(),"Some value is Empty",Snackbar.LENGTH_SHORT).show();
        }
    }
}
