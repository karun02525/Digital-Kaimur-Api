package com.digital.kaimur.services;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.digital.kaimur.models.authentication.AuthRequest;
import com.digital.kaimur.models.authentication.ChangePassword;
import com.digital.kaimur.models.authentication.ForgotPassword;
import com.digital.kaimur.models.authentication.ProfileUpdate;
import com.digital.kaimur.models.authentication.RegisterDevice;
import com.digital.kaimur.models.authentication.User;


public interface UserService {
	
    ResponseEntity<?> createUser(User user);
    
    ResponseEntity<?> registerDevice(RegisterDevice reg_device);
   
    ResponseEntity<?> login(AuthRequest user);

    ResponseEntity<?> forgotPassword(ForgotPassword forgotPassword);

    ResponseEntity<?> changePassword(ChangePassword changePassword);
    
    ResponseEntity<?> profileImageUpdate(MultipartFile user_avatar);

    ResponseEntity<?> profileUpdate(ProfileUpdate profile_update);

    ResponseEntity<?> getPhoto(String path);
    
    ResponseEntity<?> venderVerify();
    
    ResponseEntity<?> venderRegister(String category_id,String category_name);
    
    ResponseEntity<?> notification();
    
    
    
    
    
    
    
   
    
}