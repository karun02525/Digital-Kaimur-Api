package com.digital.kaimur.services.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CategoryService{
     ResponseEntity<?> categorySave(String cname,int pos, MultipartFile avatar);
     ResponseEntity<?> findAllCategory();
     ResponseEntity<?> findByCategory(String cid);
     ResponseEntity<?> getPhoto(String path) throws IOException;
     ResponseEntity<?> removeCategoryAvatar(String avatar_key);
     ResponseEntity<?> updateCategory(String category_id, String category_name,int category_postion, MultipartFile category_avatar);
}