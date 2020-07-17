package com.digital.kaimur.models.authentication;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Document(collection = "user")
public class User {

	@ApiModelProperty(hidden = true)
	@Id
	private ObjectId uid;

	@NotNull(message = "Name cannot be empty")
	@Size(min = 3, message = "Name must not be less than 3 characters")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Please enter valid name")
	private String name;

	
	@NotNull(message = "Email address cannot be empty")
	@Size(min = 8, max = 40, message = "enter a valid email address.")
	@Email(message = "Enter a valid email address.") 
	private String email;

	@NotNull(message = "Mobile number cannot be empty")
	@Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Please enter valid mobile number must be 10 digits")
	private String mobile;

	@NotNull(message = "gender cannot be empty")
	@ApiModelProperty(allowableValues = "male")
	private String gender;

	@NotNull(message = "password cannot be empty")
	@Size(min = 8, max = 20, message = "Password must be equal or grater than 8 characters and less than 20 characters")
	@JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@ApiModelProperty(hidden = true)
	private String user_avatar;

	@ApiModelProperty(hidden = true)
	private String token;
	
	@ApiModelProperty(hidden = true)
	private String address;
	
	@ApiModelProperty(hidden = true)
	private String pincode;

	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date create_at = new Date();

	public String getUid() {
		return uid.toHexString();
	}

}
