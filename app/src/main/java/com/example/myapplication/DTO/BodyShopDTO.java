package com.example.myapplication.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class BodyShopDTO implements Parcelable {
    String bodyshop_no;
    String bodyshop_id;
    String bodyshop_pw;
    String bodyshop_name;
    String bodyshop_address;
    String bodyshop_lat;
    String bodyshop_long;

    public BodyShopDTO() {
    }

    public BodyShopDTO(String bodyshop_no, String bodyshop_id, String bodyshop_pw, String bodyshop_name, String bodyshop_address, String bodyshop_lat, String bodyshop_long) {
        this.bodyshop_no = bodyshop_no;
        this.bodyshop_id = bodyshop_id;
        this.bodyshop_pw = bodyshop_pw;
        this.bodyshop_name = bodyshop_name;
        this.bodyshop_address = bodyshop_address;
        this.bodyshop_lat = bodyshop_lat;
        this.bodyshop_long = bodyshop_long;
    }

    protected BodyShopDTO(Parcel in) {
        bodyshop_no = in.readString();
        bodyshop_id = in.readString();
        bodyshop_pw = in.readString();
        bodyshop_name = in.readString();
        bodyshop_address = in.readString();
        bodyshop_lat = in.readString();
        bodyshop_long = in.readString();
    }

    public static final Creator<BodyShopDTO> CREATOR = new Creator<BodyShopDTO>() {
        @Override
        public BodyShopDTO createFromParcel(Parcel in) {
            return new BodyShopDTO(in);
        }

        @Override
        public BodyShopDTO[] newArray(int size) {
            return new BodyShopDTO[size];
        }
    };

    public String getBodyshop_no() {
        return bodyshop_no;
    }

    public void setBodyshop_no(String bodyshop_no) {
        this.bodyshop_no = bodyshop_no;
    }

    public String getBodyshop_id() {
        return bodyshop_id;
    }

    public void setBodyshop_id(String bodyshop_id) {
        this.bodyshop_id = bodyshop_id;
    }

    public String getBodyshop_pw() {
        return bodyshop_pw;
    }

    public void setBodyshop_pw(String bodyshop_pw) {
        this.bodyshop_pw = bodyshop_pw;
    }

    public String getBodyshop_name() {
        return bodyshop_name;
    }

    public void setBodyshop_name(String bodyshop_name) {
        this.bodyshop_name = bodyshop_name;
    }

    public String getBodyshop_address() {
        return bodyshop_address;
    }

    public void setBodyshop_address(String bodyshop_address) {
        this.bodyshop_address = bodyshop_address;
    }

    public String getBodyshop_lat() {
        return bodyshop_lat;
    }

    public void setBodyshop_lat(String bodyshop_lat) {
        this.bodyshop_lat = bodyshop_lat;
    }

    public String getBodyshop_long() {
        return bodyshop_long;
    }

    public void setBodyshop_long(String bodyshop_long) {
        this.bodyshop_long = bodyshop_long;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bodyshop_no);
        dest.writeString(bodyshop_id);
        dest.writeString(bodyshop_pw);
        dest.writeString(bodyshop_name);
        dest.writeString(bodyshop_address);
        dest.writeString(bodyshop_lat);
        dest.writeString(bodyshop_long);
    }
}
