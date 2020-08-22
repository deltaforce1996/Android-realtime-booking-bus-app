package com.example.alifdeltaforce.realtimebooking.Model;


import java.util.List;

/**
 * Created by Alif Delta Force on 7/23/2018.
 */

public class Booking {


    /**
     * status : 1
     * massage : show success
     * booking : [{"booking_id":"123456789as3","bus_id":"UTD-2653","tel_passenger":"0876292036","amount_seat":"1","location_long":"123","location_lah":"12312","status":"0","ID":"16","TripsName":"Puket - SuratThani"}]
     */

    private int status;
    private String massage;
    private List<BookingBean> booking;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public List<BookingBean> getBooking() {
        return booking;
    }

    public void setBooking(List<BookingBean> booking) {
        this.booking = booking;
    }

    public static class BookingBean {
        /**
         * booking_id : 123456789as3
         * bus_id : UTD-2653
         * tel_passenger : 0876292036
         * amount_seat : 1
         * location_long : 123
         * location_lah : 12312
         * status : 0
         * ID : 16
         * TripsName : Puket - SuratThani
         */

        private String booking_id;
        private String bus_id;
        private String tel_passenger;
        private String amount_seat;
        private String location_long;
        private String location_lah;
        private String status;
        private String ID;
        private String TripsName;

        public String getBooking_id() {
            return booking_id;
        }

        public void setBooking_id(String booking_id) {
            this.booking_id = booking_id;
        }

        public String getBus_id() {
            return bus_id;
        }

        public void setBus_id(String bus_id) {
            this.bus_id = bus_id;
        }

        public String getTel_passenger() {
            return tel_passenger;
        }

        public void setTel_passenger(String tel_passenger) {
            this.tel_passenger = tel_passenger;
        }

        public String getAmount_seat() {
            return amount_seat;
        }

        public void setAmount_seat(String amount_seat) {
            this.amount_seat = amount_seat;
        }

        public String getLocation_long() {
            return location_long;
        }

        public void setLocation_long(String location_long) {
            this.location_long = location_long;
        }

        public String getLocation_lah() {
            return location_lah;
        }

        public void setLocation_lah(String location_lah) {
            this.location_lah = location_lah;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getTripsName() {
            return TripsName;
        }

        public void setTripsName(String TripsName) {
            this.TripsName = TripsName;
        }
    }
}
