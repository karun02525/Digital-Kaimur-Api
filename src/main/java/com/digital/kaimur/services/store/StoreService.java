package com.digital.kaimur.services.store;

import com.digital.kaimur.models.StoreModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
    ResponseEntity<?> saveStore(MultipartFile[] files, StoreModel storeModel);

}
