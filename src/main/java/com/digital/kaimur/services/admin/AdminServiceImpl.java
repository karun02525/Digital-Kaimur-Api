package com.digital.kaimur.services.admin;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.digital.kaimur.models.NotificationModel;
import com.digital.kaimur.models.authentication.VenderVerifyModel;
import com.digital.kaimur.models.authentication.VerifyModel;
import com.digital.kaimur.models.common.ResponseArrayModel;
import com.digital.kaimur.models.common.ResponseModel;

import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    private final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);



    @Override
    public ResponseEntity<?> isVerify(VerifyModel venderModel) {
    	String vender_id =venderModel.getVender_id();
    	int is_verify =Integer.parseInt(venderModel.getIs_verify());
    	String uid = venderModel.getUid();
    	
        log.info("Admin" + venderModel);
    	
        String message="";
        VenderVerifyModel vendeId = mongoTemplate.findOne(new Query(Criteria.where("_id").is(vender_id.trim()).and("uid").is(uid.trim())), VenderVerifyModel.class);
        if (vendeId == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User id/Vender id invalid! please try again"), HttpStatus.BAD_REQUEST);
        } else {
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("_id").is(vender_id.trim()));
            Update updateVenderRegister = new Update().set("is_verify", is_verify);
            mongoTemplate.upsert(query1, updateVenderRegister, VenderVerifyModel.class);
            
            if(is_verify==1){
                message="Your shop is pending...";
                saveVerifyVender(uid,vender_id,vendeId.getCategory_name(),"Pending",message);
            }
            if(is_verify==2){
                message="Your shop has been active successfully";
                saveVerifyVender(uid,vender_id,vendeId.getCategory_name(),"Approved",message); 
            }
            if(is_verify==3){
                message="Your shop has been rejected";
                saveVerifyVender(uid,vender_id,vendeId.getCategory_name(),"Rejected",message);
            }
            return new ResponseEntity<>(new ResponseModel(true, message), HttpStatus.OK);     
     }
  }
    
    private void saveVerifyVender(String uid,String vernder_id,String category_name,String title,String message) {
    	
    	NotificationModel noti=new NotificationModel();
    	noti.setUid(uid);
    	noti.setVender_id(vernder_id);
    	noti.setCategory(category_name);
    	noti.setTitle(title);
    	noti.setType("Vender Register for Shop");
    	noti.setMessage(message);
    	mongoTemplate.save(noti);
        // sendMail("karunkumar02525@gmail.com","Rejected",message);
    }

    @Override
    public ResponseEntity<?> adminService() {
        List<VenderVerifyModel> vender = mongoTemplate.findAll(VenderVerifyModel.class);
        if (vender.isEmpty())
            return new ResponseEntity<>(new ResponseArrayModel(false, "no data available"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ResponseArrayModel(true, "list all pending vender", vender), HttpStatus.OK);
    }
}
