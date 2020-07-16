package com.digital.kaimur.controllers;

import com.digital.kaimur.models.StoreModel;
import com.digital.kaimur.models.common.ResponseModel;
import com.digital.kaimur.services.store.StoreService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final Logger log = LoggerFactory.getLogger(StoreController.class);


    @Autowired
    private StoreService storeService;


    @GetMapping("/all-store")
    public ResponseEntity<?> findAllCategory() {
        return storeService.findAllStore();
    }


    @PostMapping("/create-store")
    public ResponseEntity<?> saveStore(@RequestParam("images") MultipartFile[] files,
                                       @RequestParam("cid") String cid,
                                       @RequestParam("vid") String vid,
                                       @RequestParam("sname") String sname,
                                       @RequestParam("semail") String semail,
                                       @RequestParam("smobile") String smobile,
                                       @RequestParam("color") String color,
                                       @RequestParam("latitude") double latitude,
                                       @RequestParam("longitude") double longitude,
                                       @RequestParam("address") String address,
                                       @RequestParam("nearby") String nearby,
                                       @RequestParam("pin_code") String pin_code,
                                       @RequestParam("owner_name") String owner_name,
                                       @RequestParam("owner_mobile") String owner_mobile,
                                       @RequestParam("owner_email") String owner_email) {

        if (files[0].getSize()==0) {
            return new ResponseEntity<>(new ResponseModel(false, "file field is mandatory"), HttpStatus.EXPECTATION_FAILED);
        } else if (cid.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "cid field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (vid.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "vid field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (sname.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "store name field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (smobile.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "store mobile field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (semail.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "store email field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (color.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "color field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (latitude==0.0) {
            return new ResponseEntity<>(new ResponseModel(false, "latitude field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (longitude==0.0) {
            return new ResponseEntity<>(new ResponseModel(false, "longitude field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (address.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "address field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (nearby.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "nearby field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (pin_code.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "pin code field is mandatory"), HttpStatus.BAD_REQUEST);
        } else if (owner_name.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "owner name field is mandatory"), HttpStatus.BAD_REQUEST);
        } else if (owner_mobile.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "owner mobile field is mandatory"), HttpStatus.BAD_REQUEST);
        } else if (owner_email.equals("")) {
            return new ResponseEntity<>(new ResponseModel(false, "owner email field is mandatory"), HttpStatus.BAD_REQUEST);
        } else {
            return storeService.saveStore(files,new StoreModel(cid,vid,sname,semail,smobile,color,latitude,longitude,address,nearby,pin_code,owner_name,owner_mobile,owner_email));
        }
    }



}
