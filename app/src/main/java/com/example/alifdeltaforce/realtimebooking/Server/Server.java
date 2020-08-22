package com.example.alifdeltaforce.realtimebooking.Server;


import com.example.alifdeltaforce.realtimebooking.Model.Booking;
import com.example.alifdeltaforce.realtimebooking.Model.BookingCheck;
import com.example.alifdeltaforce.realtimebooking.Model.Passenger;
import com.example.alifdeltaforce.realtimebooking.Model.TripsInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Alif Delta Force on 7/19/2018.
 */

public interface Server {

    @FormUrlEncoded
    @POST("register_passenger.php")
    Call<Passenger>PassengerRegister(@Field("name_Passnger") String passengerName,
                                     @Field("date_of_birth") String passengerDate,
                                     @Field("Tel_of_Passnger") String passengerPhone,
                                     @Field("password_Passnger") String passengerPassword,
                                     @Field("gender_Passnger") String passengerGender
    );

    @FormUrlEncoded
    @POST("Login.php")
    Call<Passenger>PassengerLogin(@Field("tel") String passengerPhone,
                                  @Field("password") String passengerPassword
    );

    @FormUrlEncoded
    @POST("showProfiles.php")
    Call<Passenger>PassengerDetail(@Field("Tel_of_Passnger") String passengerDatail);

    @GET("showTrip.php")
    Call<TripsInfo>ShowTripInfo();

    @FormUrlEncoded
    @POST("Select_Trip.php")
    Call<TripsInfo>SelectTripInfo(@Field("Select_Trip") String busID);

    @FormUrlEncoded
    @POST("select_drive.php")
    Call<Booking>SaveBooking(@Field("bookingID") String book_id,
                             @Field("bus_id") String busID,
                             @Field("tel_passenger") String telPass,
                             @Field("amount_seat") Integer amountSeat,
                             @Field("location_long") String lng,
                             @Field("location_lat") String lat
    );

    @FormUrlEncoded
    @POST("showReportAll.php")
    Call<Booking>GetMyTrip(@Field("tel_phone") String tel_phone);

    @FormUrlEncoded
    @POST("show_booking.php")
    Call<Booking>GetForPayout(@Field("PayoutID") String booking);

    @FormUrlEncoded
    @POST("passenger_Report.php")
    Call<BookingCheck> Check_status(@Field("tel_phone") String tel
    );

    @FormUrlEncoded
    @POST("UpdateStatus.php")
    Call<BookingCheck> Check_statuus(@Field("Tel") String tel,
                                    @Field("array_size") String array_size
    );
}
