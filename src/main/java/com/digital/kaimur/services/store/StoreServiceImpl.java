package com.digital.kaimur.services.store;

import com.digital.kaimur.models.Category;
import com.digital.kaimur.models.StoreModel;
import com.digital.kaimur.models.common.ResponseModel;
import com.digital.kaimur.models.common.ResponseObjectModel;
import com.digital.kaimur.models.elastic.StoreElasticModel;
import com.digital.kaimur.utils.ElasticQueryStore;
import com.digital.kaimur.utils.RedisKey;
import com.digital.kaimur.utils.StorageProperties;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
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
import java.util.*;

@Service
public class StoreServiceImpl implements StoreService {

    private final Path storePath = StorageProperties.getInstance().getSorePath();

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);


    @Autowired
    private ElasticQueryStore elasticStoreQuery;

    @Autowired
    private RedissonClient redissonClient;

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

        StoreModel sm= mongoTemplate.save(storeModel);
        StoreElasticModel s=new StoreElasticModel();
        s.setSid(sm.getSid());

        //for Category
        RMap<String, Category> map = redissonClient.getMap(RedisKey.listCategory);
        log.info("*************Categoty**** :  : ****************"+ sm.getCid());
        map.get(sm.getCid().trim());
        log.info("*********************************");


      
       // s.setCid(sm.getCid());
       // s.setCname(cat.getCname());

        s.setVid(sm.getVid());
        s.setSname(sm.getSname());
        s.setSemail(sm.getSemail());
        s.setSmobile(sm.getSmobile());
        s.setColor(sm.getColor());
        s.setLatitude(sm.getLatitude());
        s.setLongitude(sm.getLongitude());
        s.setAddress(sm.getAddress());
        s.setNearby(sm.getNearby());
        s.setPin_code(sm.getPin_code());
        s.setOwner_name(sm.getOwner_name());
        s.setOwner_email(sm.getOwner_email());
        s.setOwner_mobile(sm.getOwner_mobile());
        s.setImgarray(sm.getImgarray());

        elasticStoreQuery.createStoreElastic(s);
                 return new ResponseEntity<>(new ResponseObjectModel(true, "Your Register", storeModel), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findAllStore() {
        List<StoreElasticModel> _store;
        try {
            _store = elasticStoreQuery.getAllStore();
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseObjectModel(true, "all stores", _store), HttpStatus.OK);


    }






}