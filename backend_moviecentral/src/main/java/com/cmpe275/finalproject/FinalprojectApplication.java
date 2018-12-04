package com.cmpe275.finalproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cmpe275.finalproject.domain.users.UserProfileRepository;

@SpringBootApplication
public class FinalprojectApplication extends SpringBootServletInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(FinalprojectApplication.class);
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(FinalprojectApplication.class, args);
		logger.info("Hellp Spring Boot");
	}
	
	
}
