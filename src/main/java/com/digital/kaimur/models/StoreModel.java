package com.digital.kaimur.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@ToString
@Document(collection = "store")
public class StoreModel {

    @Id
    private ObjectId sid;
    String cid;
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
    List<String> imgarray;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_at = new Date();


    public StoreModel(String cid,String vid, String sname, String semail, String smobile, String color, double latitude, double longitude, String address, String nearby, String pin_code, String owner_name, String owner_mobile, String owner_email) {
        this.cid = cid;
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

    public String getSid() {
        return sid.toHexString();
    }
}
