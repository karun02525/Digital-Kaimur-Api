package com.digital.kaimur.models.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseModel {
    private boolean status;
    private String message;

    public ResponseModel(boolean status, String message) {
        this.setStatus(status);
        this.setMessage(message);
    }
}
