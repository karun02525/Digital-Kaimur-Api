package com.digital.kaimur.models.authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChangePassword {

    

    @NotNull(message = "current password cannot be null")
    @Size(min = 8,max = 20,message = "current password must be equal or grater than 8 characters and less than 20 characters")
    private String current_password;

    @NotNull(message = "new password cannot be null")
    @Size(min = 8,max = 20,message = "new password must be equal or grater than 8 characters and less than 20 characters")
    private String new_password;


}
