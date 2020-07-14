package com.digital.kaimur.models.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObjectModel {
    private boolean status;
    private String message;
    private Object data;

    public ResponseObjectModel(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
