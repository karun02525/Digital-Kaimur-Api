package com.digital.kaimur.models.elastic;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class StoreElasticModel {

    private String uid;
    private String name;
    private String email;
    private String mobile;
    private String gender;
    private String user_avatar;
    private String sid;
    private String cid;
    private String cname;
    private String vid;
    private String sname;
    private String semail;
    private String smobile;
    private String color;
    private double latitude;
    private double longitude;
    private String address;
    private String nearby;
    private String pin_code;
    private String owner_name;
    private String owner_mobile;
    private String owner_email;
    private List<String> imgarray;
}
