package com.digital.kaimur.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Boolean status;
    private String message;
    private Object data;
}