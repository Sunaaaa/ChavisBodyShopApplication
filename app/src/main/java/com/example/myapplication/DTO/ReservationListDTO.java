package com.example.myapplication.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class ReservationListDTO implements Parcelable {
    String reservation_no;
    String key;
    String key_expire_time;
    String member_mname;
    String car_type;
    String car_id;
    String reservation_time;
    String repaired_time;
    String repaired_person;

    public ReservationListDTO() {
    }

    public ReservationListDTO(String reservation_no, String key, String key_expire_time, String member_mname, String car_type, String car_id, String reservation_time, String repaired_time, String repaired_person) {
        this.reservation_no = reservation_no;
        this.key = key;
        this.key_expire_time = key_expire_time;
        this.member_mname = member_mname;
        this.car_type = car_type;
        this.car_id = car_id;
        this.reservation_time = reservation_time;
        this.repaired_time = repaired_time;
        this.repaired_person = repaired_person;
    }

    public String getReservation_no() {
        return reservation_no;
    }

    public void setReservation_no(String reservation_no) {
        this.reservation_no = reservation_no;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey_expire_time() {
        return key_expire_time;
    }

    public void setKey_expire_time(String key_expire_time) {
        this.key_expire_time = key_expire_time;
    }

    public String getMember_mname() {
        return member_mname;
    }

    public void setMember_mname(String member_mname) {
        this.member_mname = member_mname;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(String reservation_time) {
        this.reservation_time = reservation_time;
    }

    public String getRepaired_time() {
        return repaired_time;
    }

    public void setRepaired_time(String repaired_time) {
        this.repaired_time = repaired_time;
    }

    public String getRepaired_person() {
        return repaired_person;
    }

    public void setRepaired_person(String repaired_person) {
        this.repaired_person = repaired_person;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}