package com.digital.kaimur.models.authentication;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileUpdate {

	
	@Size(min = 8, max = 40, message = "enter a valid email address.")
	@Email(message = "Enter a valid email address.") 
	private String email;
	
	@ApiModelProperty(allowableValues = "male")
	private String gender;

	
	@Size(min = 20, max = 200, message = "Enter a valid address.")
	private String address;
	
	
	@Size(min = 6, max = 6, message = "enter a valid pin code.")
	@Pattern(regexp = "(^$|[0-9]{6})", message = "Please enter valid pin code must be 6 digits")
	private String pincode;
	

	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date create_at = new Date();

}
