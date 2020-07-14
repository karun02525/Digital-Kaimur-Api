package com.digital.kaimur.models;

public class StoreModel {
    String cid;
    String cname;
    String vid;
    String sname;
    String semail;
    String smobile;
    String color;
    double latitude;
    double longitude;
    String address;
    String nearby;
    String pin_code;
    String owner_name;
    String owner_mobile;
    String owner_email;

    public StoreModel(String cid, String cname, String vid, String sname, String semail, String smobile, String color, double latitude, double longitude, String address, String nearby, String pin_code, String owner_name, String owner_mobile, String owner_email) {
        this.cid = cid;
        this.cname = cname;
        this.vid = vid;
        this.sname = sname;
        this.semail = semail;
        this.smobile = smobile;
        this.color = color;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.nearby = nearby;
        this.pin_code = pin_code;
        this.owner_name = owner_name;
        this.owner_mobile = owner_mobile;
        this.owner_email = owner_email;
    }
}
