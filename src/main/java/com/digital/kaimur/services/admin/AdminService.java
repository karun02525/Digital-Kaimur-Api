package com.digital.kaimur.services.admin;

import org.springframework.http.ResponseEntity;

import com.digital.kaimur.models.authentication.VerifyModel;

public interface AdminService {
	
    ResponseEntity<?> isVerify(VerifyModel venderModel);

    ResponseEntity<?> adminService();
}