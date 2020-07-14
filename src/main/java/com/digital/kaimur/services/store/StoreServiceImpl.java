package com.digital.kaimur.services.store;

import com.digital.kaimur.models.StoreModel;
import com.digital.kaimur.models.common.ResponseObjectModel;
import com.digital.kaimur.utils.StorageProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class StoreServiceImpl implements StoreService {

    private final Path storePath = StorageProperties.getInstance().getSorePath();

    @Override
    public ResponseEntity<?> saveStore(MultipartFile[] files, StoreModel storeModel) {
        List<String> fileNames = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            String storeName = "store" + UUID.randomUUID() + ".png";
            try {
                Files.copy(file.getInputStream(),this.storePath.resolve(storeName), StandardCopyOption.REPLACE_EXISTING);
                fileNames.add(storeName);
            } catch (Exception e) {
                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            }
        });
        return new ResponseEntity<>(new ResponseObjectModel(true, "Your ification", fileNames.toString()), HttpStatus.OK);
    }
}