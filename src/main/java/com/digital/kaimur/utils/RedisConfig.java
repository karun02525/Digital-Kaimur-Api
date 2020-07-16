package com.digital.kaimur.utils;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RedisConfig {

	  @Value("${redis.host}") 
	    public String host;	   
	    public String getHost() {
	return host;
	}
	
   @Bean(destroyMethod = "shutdown")
    RedissonClient redissonClient() {
    	Config config = new Config();
    	config.useSingleServer().setAddress(host);
    	RedissonClient rdson = Redisson.create(config);
        return rdson;
    }
   
}