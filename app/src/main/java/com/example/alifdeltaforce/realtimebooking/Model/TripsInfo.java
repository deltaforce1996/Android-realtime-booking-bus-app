package com.example.alifdeltaforce.realtimebooking.Model;


import java.util.List;

/**
 * Created by Alif Delta Force on 7/21/2018.
 */

public class TripsInfo {

    /**
     * status : 1
     * massage : Show success
     * trip : [{"bus_id":"PG-1996","bus_seat_info":"2","name_trips":"Puket - Surat","end_long":"99.3292111158371","end_lah":"9.144543326831538","location_of_bus_long":"98.29905509948732","location_of_bus_lah":"8.202995160713005"},{"bus_id":"UT-850","bus_seat_info":"5","name_trips":"Surat - Krabi","end_long":"98.90879631042482","end_lah":"8.101548942348819","location_of_bus_long":"98.83798599243165","location_of_bus_lah":"8.42652678506588"}]
     */

    private int status;
    private String massage;
    private List<TripBean> trip;

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

    public List<TripBean> getTrip() {
        return trip;
    }

    public void setTrip(List<TripBean> trip) {
        this.trip = trip;
    }

    public static class TripBean {
        /**
         * bus_id : PG-1996
         * bus_seat_info : 2
         * name_trips : Puket - Surat
         * end_long : 99.3292111158371
         * end_lah : 9.144543326831538
         * location_of_bus_long : 98.29905509948732
         * location_of_bus_lah : 8.202995160713005
         */

        private String bus_id;
        private String bus_seat_info;
        private String name_trips;
        private String end_long;
        private String end_lah;
        private String location_of_bus_long;
        private String location_of_bus_lah;

        public String getBus_id() {
            return bus_id;
        }

        public void setBus_id(String bus_id) {
            this.bus_id = bus_id;
        }

        public String getBus_seat_info() {
            return bus_seat_info;
        }

        public void setBus_seat_info(String bus_seat_info) {
            this.bus_seat_info = bus_seat_info;
        }

        public String getName_trips() {
            return name_trips;
        }

        public void setName_trips(String name_trips) {
            this.name_trips = name_trips;
        }

        public String getEnd_long() {
            return end_long;
        }

        public void setEnd_long(String end_long) {
            this.end_long = end_long;
        }

        public String getEnd_lah() {
            return end_lah;
        }

        public void setEnd_lah(String end_lah) {
            this.end_lah = end_lah;
        }

        public String getLocation_of_bus_long() {
            return location_of_bus_long;
        }

        public void setLocation_of_bus_long(String location_of_bus_long) {
            this.location_of_bus_long = location_of_bus_long;
        }

        public String getLocation_of_bus_lah() {
            return location_of_bus_lah;
        }

        public void setLocation_of_bus_lah(String location_of_bus_lah) {
            this.location_of_bus_lah = location_of_bus_lah;
        }
    }
}
