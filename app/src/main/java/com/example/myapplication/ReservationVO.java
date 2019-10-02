package com.example.myapplication;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


// 해당 클래스의 객체가 마샬링이 가능하도록 parcelable interface를 구현
// 마샬링이 데이터 변환하는거인듯 ? 언마샬링은 반대! (마샬링된 데이터 받는쪽에서하는거)

public class ReservationVO implements Parcelable {

    private int reservation_no;
    private int bodyshop_no;
    private int member_no;
    private String key;
    private String key_expire_time;
    private String reservation_time;
    private String repaired_time;
    private String repaired_person;


    public static final Creator<ReservationVO> CREATOR = new Creator<ReservationVO>() {
        @Override
        public ReservationVO createFromParcel(Parcel parcel) {
            // 마샬링된 데이터를 언마샬링(복원)할 때 사용되는 method
            return new ReservationVO(parcel);
        }
        @Override
        public ReservationVO[] newArray(int i) {      // 몇개 복원할거에요 숫자 : i
            return new ReservationVO[i];
        }
    };


    public ReservationVO() {
    }

    public ReservationVO(int reservation_no) {
        this.reservation_no = reservation_no;
    }

    public ReservationVO(int reservation_no, int bodyshop_no, int member_no, String key, String key_expire_time, String reservation_time, String repaired_time, String repaired_person) {
        this.reservation_no = reservation_no;
        this.bodyshop_no = bodyshop_no;
        this.member_no = member_no;
        this.key = key;
        this.key_expire_time = key_expire_time;
        this.reservation_time = reservation_time;
        this.repaired_time = repaired_time;
        this.repaired_person = repaired_person;
    }

    protected ReservationVO(Parcel parcel){
        reservation_no = parcel.readInt();
        bodyshop_no = parcel.readInt();
        member_no = parcel.readInt();
        key = parcel.readString();
        key_expire_time = parcel.readString();
        reservation_time = parcel.readString();
        repaired_time = parcel.readString();
        repaired_person = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeInt(reservation_no);
            parcel.writeInt(bodyshop_no);
            parcel.writeInt(member_no);
            parcel.writeString(key);
            parcel.writeString(key_expire_time);
            parcel.writeString(reservation_time);
            parcel.writeString(repaired_time);
            parcel.writeString(repaired_person);
        } catch (Exception e){
            Log.i("LOG",e.toString());
        }
    }

    public int getReservation_no() {
        return reservation_no;
    }

    public void setReservation_no(int reservation_no) {
        this.reservation_no = reservation_no;
    }

    public int getBodyshop_no() {
        return bodyshop_no;
    }

    public void setBodyshop_no(int bodyshop_no) {
        this.bodyshop_no = bodyshop_no;
    }

    public int getMember_no() {
        return member_no;
    }

    public void setMember_no(int member_no) {
        this.member_no = member_no;
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

    public String getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(String reserve_time) {
        this.reservation_time = reserve_time;
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
}