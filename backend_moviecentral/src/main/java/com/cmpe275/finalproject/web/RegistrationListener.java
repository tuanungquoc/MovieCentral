package com.cmpe275.finalproject.web;

import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.VerificationToken;
import com.cmpe275.finalproject.domain.users.VerificationTokenRepository;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;
import com.cmpe275.finalproject.service.IUserProfileService;

@Component
public class RegistrationListener implements
ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private IUserProfileService service;
	

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;
	

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		UserProfile userProfile = event.getUserProfile();
	
		try {
			userProfile.setPassword(bCryptPasswordEncoder.encode(userProfile.getPassword()));
			service.saveRegisteredUser(userProfile,null);
			String tokenStr = UUID.randomUUID().toString();
			service.createVerificationToken(userProfile, tokenStr);
			String recipientAddress = userProfile.getUsername();
			String subject = "Registration Confirmation";
			String confirmationUrl 
			= event.getAppUrl() + "/userprofile/regitrationConfirm?token=" + tokenStr;
			//String message = messages.getMessage("message.regSucc", null, event.getLocale());
	
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(recipientAddress);
			email.setSubject(subject);
			email.setText("Please use this token: " + tokenStr);
			mailSender.send(email);
		}catch(Exception e) {
			throw new UserNotFoundException("User already exists");
		}
	}
}