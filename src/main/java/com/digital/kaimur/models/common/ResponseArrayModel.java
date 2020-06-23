package com.digital.kaimur.models.common;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ResponseArrayModel {
    private boolean status;
    private String message;
    private List<?> data;

    public ResponseArrayModel(boolean status, String message) {
        this.setStatus(status);
        this.setMessage(message);
    }

   public ResponseArrayModel(boolean status, String message, List<?> data) {
        this.setStatus(status);
        this.setMessage(message);
        this.setData(data);
    }
 }