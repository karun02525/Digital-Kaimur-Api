package com.digital.kaimur.models.authentication;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ForgotPassword {

    @NotNull(message = "Mobile number cannot be null")
    @Size(min = 10,max = 10,message = "Mobile number must be 10 digits")
    @Pattern(regexp="(^$|[0-9]{10})", message = "Please enter valid mobile number must be 10 digits")
    private String mobile;

    @NotNull(message = "Email address cannot be empty")
	@Size(min = 8, max = 40, message = "enter a valid email address.")
	@Email(message = "Enter a valid email address.") 
	private String email;

}
