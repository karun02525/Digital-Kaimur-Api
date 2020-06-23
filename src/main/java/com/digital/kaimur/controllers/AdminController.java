package com.digital.kaimur.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digital.kaimur.models.authentication.VerifyModel;
import com.digital.kaimur.services.admin.AdminService;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("/verify")
    public ResponseEntity<?> getStoreDetails(@Valid @RequestBody  VerifyModel venderModel) {
        return adminService.isVerify(venderModel);
    }

    @GetMapping("/get-status-verify")
    public ResponseEntity<?> adminService() {
        return adminService.adminService();
    }
}
