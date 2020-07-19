package com.digital.kaimur.services.store;

import com.digital.kaimur.models.Category;
import com.digital.kaimur.models.StoreModel;
import com.digital.kaimur.models.authentication.User;
import com.digital.kaimur.models.common.ResponseModel;
import com.digital.kaimur.models.common.ResponseObjectModel;
import com.digital.kaimur.models.elastic.StoreElasticModel;
import com.digital.kaimur.services.UserServiceImpl;
import com.digital.kaimur.utils.ElasticQueryStore;
import com.digital.kaimur.utils.RedisKey;
import com.digital.kaimur.utils.StorageProperties;
import com.digital.kaimur.utils.Utils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private ElasticQueryStore elasticStoreQuery;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserServiceImpl userService;



    @Override
    public ResponseEntity<?> saveStore(MultipartFile[] imgarray, StoreModel storeModel) {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> fileNames = new ArrayList<>();
        Arrays.stream(imgarray).forEach(file -> {
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

        User user= userService.getUserDetailRaw(uid);
        s.setUid(user.getUid());
        s.setName(user.getName());
        s.setEmail(user.getEmail());
        s.setMobile(user.getMobile());
        s.setGender(user.getGender());
        s.setUser_avatar(user.getUser_avatar());

        Category cat= Utils.getCategory(sm.getCid(),redissonClient.getList(RedisKey.listCategory));
        s.setCid(sm.getCid());
        s.setCname(cat.getCname());

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

        log.info("************************************************");
        log.info(s.toString());
        log.info("************************************************");

        elasticStoreQuery.createStoreElastic(s);
                 return new ResponseEntity<>(new ResponseObjectModel(true, "Your Register", s), HttpStatus.OK);
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


    /*Get Photo by path*/
    @Override
    public ResponseEntity<?> getPhoto(String path) {
        Path imaPath = Paths.get(storePath + "\\" + path);
        if (Files.exists(imaPath)) {
            return Utils.getImageLoad(imaPath);
        } else {
            return new ResponseEntity<>(new ResponseModel(false, "image path not exists"), HttpStatus.NOT_FOUND);
        }
    }

}