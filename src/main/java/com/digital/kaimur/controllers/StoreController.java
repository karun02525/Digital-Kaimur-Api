package com.digital.kaimur.controllers;

import com.digital.kaimur.models.StoreModel;
import com.digital.kaimur.models.common.ResponseModel;
import com.digital.kaimur.services.store.StoreService;
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
    public ResponseEntity<?> saveStore(@RequestParam("imgarray") MultipartFile[] imgarray,
                                       @RequestParam("cid") String cid,
                                       @RequestParam("vid") String vid,
                                       @RequestParam("sname") String sname,
                                       @RequestParam("semail") String semail,
                                       @RequestParam("smobile") String smobile,
                                       @RequestParam("color") String color,
                                       @RequestParam("latitude") String latitude,
                                       @RequestParam("longitude") String longitude,
                                       @RequestParam("address") String address,
                                       @RequestParam("nearby") String nearby,
                                       @RequestParam("pin_code") String pin_code,
                                       @RequestParam("owner_name") String owner_name,
                                       @RequestParam("owner_mobile") String owner_mobile,
                                       @RequestParam("owner_email") String owner_email) {

        if (imgarray[0].getSize()==0) {
            return new ResponseEntity<>(new ResponseModel(false, "file field is mandatory"), HttpStatus.EXPECTATION_FAILED);
        } if (cid.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "cid field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (vid.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "vid field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (sname.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "store name field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (smobile.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "store mobile field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (semail.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "store email field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (color.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "color field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (address.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "address field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (nearby.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "nearby field is mandatory"), HttpStatus.BAD_REQUEST);
        }else if (pin_code.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "pin code field is mandatory"), HttpStatus.BAD_REQUEST);
        } else if (owner_name.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "owner name field is mandatory"), HttpStatus.BAD_REQUEST);
        } else if (owner_mobile.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "owner mobile field is mandatory"), HttpStatus.BAD_REQUEST);
        } else if (owner_email.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "owner email field is mandatory"), HttpStatus.BAD_REQUEST);
        } else {
            return storeService.saveStore(imgarray,new StoreModel(cid,vid,sname,semail,smobile,color,Double.parseDouble(latitude),Double.parseDouble(longitude),address,nearby,pin_code,owner_name,owner_mobile,owner_email));
        }
    }



}
