package com.test.GraphAPITest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphApiTestApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(GraphApiTestApplication.class, args);
		UploadToSharePoint uploadToSharePoint = new UploadToSharePoint();
        uploadToSharePoint.getFileMac();
//		  uploadToSharePoint.getFileMacOriginal();

	}

}
