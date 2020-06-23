package com.digital.kaimur.security;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.digital.kaimur.models.authentication.User;


@Service
public class MyUserDetails implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(MyUserDetails.class);

	@Autowired
	private MongoTemplate mongoTemplate;


	@Override
	public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException { 
    final User user = mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid.trim())), User.class);
    
    if (user == null) { 
    	log.info("user destails"+user);
	       throw new UsernameNotFoundException("Unauthorized Please try again!!");
	}
    return new org.springframework.security.core.userdetails.User(user.getUid(),user.getPassword(),new ArrayList<>());
	 
  }
}
