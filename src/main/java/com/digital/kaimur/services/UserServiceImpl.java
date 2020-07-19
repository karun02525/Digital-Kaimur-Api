package com.digital.kaimur.services;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.digital.kaimur.models.Category;
import com.digital.kaimur.utils.ElasticQueryStore;
import com.digital.kaimur.utils.RedisKey;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.digital.kaimur.exception.CustomException;
import com.digital.kaimur.models.NotificationModel;
import com.digital.kaimur.models.authentication.AuthRequest;
import com.digital.kaimur.models.authentication.ChangePassword;
import com.digital.kaimur.models.authentication.ForgotPassword;
import com.digital.kaimur.models.authentication.ProfileUpdate;
import com.digital.kaimur.models.authentication.RegisterDevice;
import com.digital.kaimur.models.authentication.User;
import com.digital.kaimur.models.authentication.VenderVerifyModel;
import com.digital.kaimur.models.common.ResponseArrayModel;
import com.digital.kaimur.models.common.ResponseModel;
import com.digital.kaimur.models.common.ResponseObjectModel;
import com.digital.kaimur.security.JwtTokenProvider;
import com.digital.kaimur.utils.StorageProperties;
import com.digital.kaimur.utils.Utils;


@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ElasticQueryStore elasticStoreQuery;


    private final Path profilePath = StorageProperties.getInstance().getProfilePath();

    public ResponseEntity<?> login(AuthRequest authRequest) {
        final User userPayload = mongoTemplate
                .findOne(new Query(Criteria.where("mobile").is(authRequest.getMobile().trim())), User.class);

        if (userPayload == null) {
            return new ResponseEntity<>(
                    new ResponseModel(false, "Mobile number or password invalid ! please try again"),
                    HttpStatus.BAD_REQUEST);
        } else {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userPayload.getUid(),
                        authRequest.getPassword().trim()));
                setRedis(userPayload);
                userPayload.setToken(jwtTokenProvider.createToken(userPayload.getUid()));
                userPayload.setPassword(null);
                return new ResponseEntity<>(new ResponseObjectModel(true, "Your login successfully", userPayload),
                        HttpStatus.OK);

            } catch (AuthenticationException e) {
                return new ResponseEntity<>(
                        new ResponseModel(false, "Mobile number or password invalid ! please try again"),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public ResponseEntity<?> createUser(User user) {
        User checkUserMobile;
        checkUserMobile = mongoTemplate.findOne(new Query(Criteria.where("mobile").is(user.getMobile().trim())),
                User.class);
        if (checkUserMobile == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
            mongoTemplate.save(user);
            setRedis(user);
            user.setPassword(null);
            user.setToken(jwtTokenProvider.createToken(user.getUid()));
            return new ResponseEntity<>(new ResponseObjectModel(true, "Your registration successfully", user),
                    HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>(new ResponseModel(false, "Mobile is already in use"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @Override
    public ResponseEntity<?> getUserDetail(String uid) {
        User checkUser = getUserDetailRaw(uid);
        if (checkUser == null)
            return new ResponseEntity<>(new ResponseModel(false, "no user found"), HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(new ResponseObjectModel(true, "User details", checkUser), HttpStatus.OK);
    }

    @Override
    public User getUserDetailRaw(String uid) {
        User checkUser = null;
        RMap<String, User> map = redissonClient.getMap(RedisKey.userDetail);
        if (map != null) {
            checkUser = map.get(uid);
            if (checkUser == null) {
                checkUser = mongoTemplate.findOne(new Query(new Criteria().and("_id").is(uid)), User.class);
            }
        }
        return checkUser;
    }

    public void setRedis(User payload) {
        RMap<String, User> map = redissonClient.getMap(RedisKey.userDetail);
        map.fastPut(payload.getUid(), payload);
    }


    @Override
    public ResponseEntity<?> registerDevice(RegisterDevice reg_device) {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        User model = mongoTemplate.findOne(new Query(Criteria.where("_id").is(uid.trim())), User.class);
        if (model == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User id invalid! please try again"), HttpStatus.BAD_REQUEST);
        } else {

            RegisterDevice regModel = mongoTemplate.findOne(new Query(Criteria.where("device_id").is(reg_device.getDevice_id())), RegisterDevice.class);
            if (regModel == null) {
                reg_device.setUid(uid);
                mongoTemplate.save(reg_device);
            } else {
                Query query = new Query();
                query.addCriteria(Criteria.where("device_id").is(reg_device.getDevice_id()));
                Update update = new Update().set("firebase_token", reg_device.getFirebase_token())
                        .set("updated_at", new Date());
                mongoTemplate.upsert(query, update, RegisterDevice.class);

            }
            return new ResponseEntity<>(new ResponseModel(true, "Device has been updated successfully"), HttpStatus.OK);
        }

    }


    @Override
    public ResponseEntity<?> changePassword(ChangePassword changePassword) {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        User user;
        user = mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid.trim())), User.class);
        if (user == null) {
            return new ResponseEntity<>(new ResponseModel(false, "Your uid invalid ! please try again"), HttpStatus.NOT_FOUND);
        } else {
            if (!passwordEncoder.matches(changePassword.getCurrent_password(), user.getPassword())) {
                return new ResponseEntity<>(new ResponseModel(false, "Your current password invalid ! please try again"), HttpStatus.NOT_FOUND);
            } else {
                Query query1 = new Query();
                query1.addCriteria(Criteria.where("uid").is(uid.trim()));
                Update update = new Update().set("password", passwordEncoder.encode(changePassword.getNew_password().trim()));
                mongoTemplate.upsert(query1, update, User.class);
                return new ResponseEntity<>(new ResponseModel(true, "Your password has been changed successfully"), HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<?> forgotPassword(ForgotPassword forgotPassword) {
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        User user;
        criteria.andOperator(Criteria.where("mobile").is(forgotPassword.getMobile().trim()), Criteria.where("email").is(forgotPassword.getEmail().trim()));
        user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            return new ResponseEntity<>(new ResponseModel(false, "Your mobile number or email address invalid ! please try again"), HttpStatus.BAD_REQUEST);
        } else {
            query.addCriteria(Criteria.where("mobile").is(forgotPassword.getMobile().trim()));
            final String rendomPassGenerate = "" + new Random().nextInt(931403200);
            Update update = new Update().set("password", passwordEncoder.encode(rendomPassGenerate));
            mongoTemplate.upsert(query, update, User.class);
            return new ResponseEntity<>(new ResponseModel(true, "Your password reset successfully and your temporary password " + rendomPassGenerate), HttpStatus.OK);
        }
    }


    @Override
    public ResponseEntity<?> profileUpdate(ProfileUpdate profile_update) {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        User model = mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid.trim())), User.class);
        if (model == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User id invalid! please try again"), HttpStatus.BAD_REQUEST);
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("uid").is(uid.trim()));
            Update update = new Update().set("email", profile_update.getEmail())
                    .set("gender", profile_update.getGender())
                    .set("address", profile_update.getAddress())
                    .set("pincode", profile_update.getPincode());
            mongoTemplate.upsert(query, update, User.class);

            return new ResponseEntity<>(new ResponseObjectModel(true, "profile has been updated successfully", profile_update), HttpStatus.OK);

        }
    }


    @Override
    public ResponseEntity<?> profileImageUpdate(MultipartFile user_avatar) {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("uid" + uid);
        User model = mongoTemplate.findOne(new Query(Criteria.where("_id").is(uid.trim())), User.class);
        if (model == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User id invalid! please try again"), HttpStatus.BAD_REQUEST);
        } else {
            if (model.getUser_avatar() != null && !model.getUser_avatar().equals("")) {
                Path imaPath = Paths.get(profilePath + "\\" + model.getUser_avatar());
                FileSystemUtils.deleteRecursively(imaPath.toFile());
            }
            String avatarUser = "profile_" + UUID.randomUUID() + ".png";
            try {
                try (InputStream inputStream = user_avatar.getInputStream()) {
                    Files.copy(inputStream, this.profilePath.resolve(avatarUser), StandardCopyOption.REPLACE_EXISTING);
                }
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(uid.trim()));
                Update update = new Update().set("user_avatar", avatarUser);
                mongoTemplate.upsert(query, update, User.class);
                Map<String, String> map = new HashMap<>();
                map.put("user_avatar", avatarUser);
                elasticStoreQuery.updateProfileImageElastic(uid.trim(), "user_avatar", avatarUser);
                return new ResponseEntity<>(new ResponseObjectModel(true, "Your profile image has been updated successfully", map), HttpStatus.OK);
            } catch (Exception e) {
                throw new CustomException("Failed to update empty file");
            }
        }
    }

    @Override
    public ResponseEntity<?> getPhoto(String path) {
        Path imaPath = Paths.get(profilePath + "\\" + path);
        if (Files.exists(imaPath)) {
            return Utils.getImageLoad(imaPath);
        } else {
            return new ResponseEntity<>(new ResponseModel(false, "image path not exists"), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<?> vendorVerify() {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        User model = mongoTemplate.findOne(new Query(Criteria.where("_id").is(uid.trim())), User.class);
        if (model == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User id invalid! please try again"), HttpStatus.BAD_REQUEST);
        } else {
            VenderVerifyModel result = mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid.trim())), VenderVerifyModel.class);
            if (result == null) {
                Map<String, Integer> map = new HashMap<>();
                map.put("is_verify", 0);
                return new ResponseEntity<>(new ResponseObjectModel(true, "First time create", map), HttpStatus.OK);
            } else {
                String message = "";
                if (result.getIs_verify() == 1) {
                    message = "Your Verification Pending";
                }
                if (result.getIs_verify() == 3) {
                    message = "Your Shop has been rejected";
                }
                return new ResponseEntity<>(new ResponseObjectModel(true, message, result), HttpStatus.OK);
            }
        }

    }


    @Transactional
    @Override
    public ResponseEntity<?> vendorRegister(String cid) {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        User model = mongoTemplate.findOne(new Query(Criteria.where("_id").is(uid.trim())), User.class);
        if (model == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User id invalid! please try again"), HttpStatus.BAD_REQUEST);
        } else {

            VenderVerifyModel vmodel = mongoTemplate.findOne(new Query(Criteria.where("mobile").is(model.getMobile())), VenderVerifyModel.class);
            if (vmodel != null) {
                return new ResponseEntity<>(new ResponseModel(false, "Your store already registered"), HttpStatus.BAD_REQUEST);
            } else {

                VenderVerifyModel verify = new VenderVerifyModel();
                verify.setUid(uid);
                verify.setName(model.getName());
                verify.setMobile(model.getMobile());
                verify.setEmail(model.getEmail());

                Category cat= Utils.getCategory(cid.trim(),redissonClient.getList(RedisKey.listCategory));
                verify.setCid(cid.trim());
                verify.setCname(cat.getCname());
                verify.setCavatar(cat.getAvatar());


                verify.setIs_verify(1);//Pending verification (1) means
                mongoTemplate.save(verify);

                NotificationModel noti = new NotificationModel();
                noti.setUid(uid);
                noti.setVid(verify.getVid());
                noti.setCategory(cat.getCname());
                noti.setTitle("Pending");
                noti.setType("Vender Register for Shop");
                noti.setMessage("Your verification pending.");
                mongoTemplate.save(noti);
                return new ResponseEntity<>(new ResponseObjectModel(true, "Your verification pending", verify), HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<?> notification() {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        List<NotificationModel> mess = mongoTemplate.find(new Query(Criteria.where("uid").is(uid.trim())), NotificationModel.class);
        if (mess.isEmpty()) {
            return new ResponseEntity<>(new ResponseModel(false, "No data available"), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ResponseArrayModel(true, "All Notifications", mess), HttpStatus.OK);
        }
    }


}

