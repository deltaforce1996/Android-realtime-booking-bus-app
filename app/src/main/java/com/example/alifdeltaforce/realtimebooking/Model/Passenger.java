package com.example.alifdeltaforce.realtimebooking.Model;

import java.util.List;

/**
 * Created by Alif Delta Force on 7/19/2018.
 */

public class Passenger {


    /**
     * status : 1
     * massage : Longin success
     * PassengerDeltial : [{"Tel_of_Passnger":"08454521","name_Passnger":"fgfg","gender_Passnger":"ชาย","password_Passnger":"0123456","date_of_birth":"dfdfdf","picture_Passnger":""}]
     */

    private int status;
    private String massage;
    private List<PassengerDeltialBean> PassengerDeltial;

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

    public List<PassengerDeltialBean> getPassengerDeltial() {
        return PassengerDeltial;
    }

    public void setPassengerDeltial(List<PassengerDeltialBean> PassengerDeltial) {
        this.PassengerDeltial = PassengerDeltial;
    }

    public static class PassengerDeltialBean {
        /**
         * Tel_of_Passnger : 08454521
         * name_Passnger : fgfg
         * gender_Passnger : ชาย
         * password_Passnger : 0123456
         * date_of_birth : dfdfdf
         * picture_Passnger :
         */

        private String Tel_of_Passnger;
        private String name_Passnger;
        private String gender_Passnger;
        private String password_Passnger;
        private String date_of_birth;
        private String picture_Passnger;

        public String getTel_of_Passnger() {
            return Tel_of_Passnger;
        }

        public void setTel_of_Passnger(String Tel_of_Passnger) {
            this.Tel_of_Passnger = Tel_of_Passnger;
        }

        public String getName_Passnger() {
            return name_Passnger;
        }

        public void setName_Passnger(String name_Passnger) {
            this.name_Passnger = name_Passnger;
        }

        public String getGender_Passnger() {
            return gender_Passnger;
        }

        public void setGender_Passnger(String gender_Passnger) {
            this.gender_Passnger = gender_Passnger;
        }

        public String getPassword_Passnger() {
            return password_Passnger;
        }

        public void setPassword_Passnger(String password_Passnger) {
            this.password_Passnger = password_Passnger;
        }

        public String getDate_of_birth() {
            return date_of_birth;
        }

        public void setDate_of_birth(String date_of_birth) {
            this.date_of_birth = date_of_birth;
        }

        public String getPicture_Passnger() {
            return picture_Passnger;
        }

        public void setPicture_Passnger(String picture_Passnger) {
            this.picture_Passnger = picture_Passnger;
        }
    }
}
