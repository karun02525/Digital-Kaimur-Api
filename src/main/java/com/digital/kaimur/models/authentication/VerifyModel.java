package com.digital.kaimur.models.authentication;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VerifyModel {

	@NotNull(message = "User id cannot be empty")
    @Size(min = 10, max = 60, message = "User id must be valid")
    private String uid;
	  
	@NotNull(message = "Vender id cannot be empty")
    @Size(min = 10, max = 50, message = "Vender id must be valid")
    private String vender_id;
	
	
	@NotNull(message = "Verify code cannot be empty")
    @Size(min = 1, max = 1, message = "Verify code must be valid")
    @Pattern(regexp = "(^$|[0-3])", message = "Please enter valid Verify code.")
    private String is_verify;
}
