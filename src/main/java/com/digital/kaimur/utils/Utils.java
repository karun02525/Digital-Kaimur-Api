package com.digital.kaimur.utils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class Utils {
	
	
	 public static void ImageSave(MultipartFile imagePath, String value) {
	        try {
	            byte[] bytes = imagePath.getBytes();
	            Path path = Paths.get(value);
	            Files.write(path, bytes);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public static ResponseEntity<?> getImageLoad(Path imaPath ){
	        byte[] image = new byte[0];
	        HttpHeaders headers;
	        try {
	            image = Files.readAllBytes(imaPath);
	            headers = new HttpHeaders();
	            headers.setContentType(MediaType.IMAGE_JPEG);
	            headers.setContentLength(image.length);
	            return new ResponseEntity<>(image, headers, HttpStatus.OK);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(image, null, HttpStatus.BAD_REQUEST);
	        }
	    }
	
	 public static String timeStamp(){
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        Instant instant = timestamp.toInstant();
	        long times=instant.toEpochMilli();
	        return String.valueOf(times);
	    }
	 
}
