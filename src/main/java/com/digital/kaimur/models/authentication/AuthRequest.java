package com.digital.kaimur.models.authentication;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AuthRequest {

    @NotNull(message = "Mobile number cannot be empty")
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Please enter valid mobile number must be 10 digits")
    private String mobile;

    @NotNull(message = "password cannot be empty")
    @Size(min = 8, max = 20, message = "Password must be equal or grater than 8 characters and less than 20 characters")
    private String password;

}
