package com.digital.kaimur.services.store;

import com.digital.kaimur.models.StoreModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoreService {

    ResponseEntity<?> saveStore(MultipartFile[] files, StoreModel storeModel);
    ResponseEntity<?> findAllStore();
    ResponseEntity<?> getPhoto(String path) throws IOException;
}
