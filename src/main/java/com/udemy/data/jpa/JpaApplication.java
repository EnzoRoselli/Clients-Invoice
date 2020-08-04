package com.udemy.data.jpa;

import com.udemy.data.jpa.services.interfaces.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaApplication implements CommandLineRunner {

	@Autowired
	IUploadFileService uploadFileService;

//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();

//		String password="12345";
//
//		for (int i = 0; i<2; i++){
//			String bcryptPassword = passwordEncoder.encode(password);
//			System.out.println(bcryptPassword);
//		}
	}
}
