package com.digital.kaimur.services.store;

import com.digital.kaimur.models.StoreModel;
import com.digital.kaimur.models.common.ResponseObjectModel;
import com.digital.kaimur.utils.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);



    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseEntity<?> saveStore(MultipartFile[] files, StoreModel storeModel) {
        List<String> fileNames = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            String storeName = "store_" + UUID.randomUUID() + ".png";
            try {
                Files.copy(file.getInputStream(),this.storePath.resolve(storeName), StandardCopyOption.REPLACE_EXISTING);
                fileNames.add(storeName);
            } catch (Exception e) {
                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            }
        });
        storeModel.setImgarray(fileNames);
        mongoTemplate.save(storeModel);


        log.info("*********************************");
        log.info(storeModel.toString());
        log.info("*********************************");


        return new ResponseEntity<>(new ResponseObjectModel(true, "Your Register", storeModel), HttpStatus.OK);
    }
}