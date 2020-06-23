package com.digital.kaimur.models.authentication;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "vender_verify")
public class VenderVerifyModel {
    

    @Id
    private String vender_id;
    private String uid;
    private String name;
    private String mobile;
    private String category_id;
    private String category_name;
    private int is_verify;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date create_at=new Date();

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date update_at=new Date();

}
