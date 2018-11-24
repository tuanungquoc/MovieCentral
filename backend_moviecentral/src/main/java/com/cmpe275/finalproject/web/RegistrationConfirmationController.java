package com.cmpe275.finalproject.web;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.finalproject.domain.UserProfile;
import com.cmpe275.finalproject.domain.VerificationToken;
import com.cmpe275.finalproject.service.IUserProfileService;

@RestController
@RequestMapping("/userprofile")
public class RegistrationConfirmationController {
	@Autowired
	private IUserProfileService service;
	
	
	@RequestMapping(value = "/regitrationConfirm", method = RequestMethod.GET)
	public UserProfile confirmRegistration(@RequestParam("token") String token) {
	    
	    VerificationToken verificationToken = service.getVerificationToken(token);
	    if (verificationToken == null) {
	        //throw the error
	    }
	     
	    
	    UserProfile userProfile = service.getUserProfile(verificationToken.getToken());
	     
	    userProfile.setEnabled(true); 
	    service.saveRegisteredUser(userProfile);
	    return userProfile;
	
	}
}