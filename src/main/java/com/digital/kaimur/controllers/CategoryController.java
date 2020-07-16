package com.digital.kaimur.controllers;


import java.io.IOException;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.digital.kaimur.models.common.ResponseModel;
import com.digital.kaimur.services.category.CategoryService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService category;


    @PostMapping("/create-category")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> createCategory(
            @RequestParam("cname") String cname,
            @RequestParam("pos") int pos,
            @RequestParam("avatar") MultipartFile avatar) {

        if (avatar.getSize()==0) {
            return new ResponseEntity<>(new ResponseModel(false, "file field is mandatory"), HttpStatus.EXPECTATION_FAILED);
        } else if (cname.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "category cannot be empty"), HttpStatus.BAD_REQUEST);
        } else if (cname.length() < 3) {
            return new ResponseEntity<>(new ResponseModel(false, "name must not be less than 3 characters"), HttpStatus.BAD_REQUEST);
        } else if (Pattern.matches("^[a-zA-Z]+$ ", cname)) {
            return new ResponseEntity<>(new ResponseModel(false, "Please enter valid category name"), HttpStatus.BAD_REQUEST);
        }else if (pos <=0) {
            return new ResponseEntity<>(new ResponseModel(false, "please enter valid category postion"), HttpStatus.BAD_REQUEST);
        }  else if (avatar.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "Please upload category image"), HttpStatus.BAD_REQUEST);
        }else {
            return category.categorySave(cname,pos,avatar);
        }
    }

    @PostMapping("/update-category")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> updateCategory(
            @RequestParam("cid") String cid,
            @RequestParam("cname") String cname,
            @RequestParam("category_postion") int category_postion,
            @RequestParam("category_avatar") MultipartFile category_avatar) {

        if (cname.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "category cannot be empty"), HttpStatus.BAD_REQUEST);
        } else if (cname.length() < 3) {
            return new ResponseEntity<>(new ResponseModel(false, "name must not be less than 3 characters"), HttpStatus.BAD_REQUEST);
        } else if (Pattern.matches("^[a-zA-Z]+$ ", cname)) {
            return new ResponseEntity<>(new ResponseModel(false, "Please enter valid category name"), HttpStatus.BAD_REQUEST);
        } else if (category_postion <=0) {
            return new ResponseEntity<>(new ResponseModel(false, "please enter valid category postion"), HttpStatus.BAD_REQUEST);
        }  else if (category_avatar.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "Please upload small image"), HttpStatus.BAD_REQUEST);
        } else {
            return category.updateCategory(cid,cname,category_postion, category_avatar);
        }
    }


    @ApiOperation(value = "Get All Category records")
    @GetMapping(value = "/get-category")
    public ResponseEntity<?> findAllCategory() {
        return category.findAllCategory();
    }


    @GetMapping("/image-category/{path}")
    @ResponseBody
    public ResponseEntity<?> getPhoto(@PathVariable("path") String path) throws IOException {
        return category.getPhoto(path);
    }


    @GetMapping("/get-category/{category_id}")
    public ResponseEntity<?> findByCategory(@PathVariable String category_id) {
        return category.findByCategory(category_id);
    }

    @GetMapping("/remove-category-avatar")
    public ResponseEntity<?> removeCategoryAvatar(
            @RequestParam String avatar_key) {
        return category.removeCategoryAvatar(avatar_key);
    }

}
