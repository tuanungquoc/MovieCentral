package com.cmpe275.finalproject.web;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.VerificationToken;
import com.cmpe275.finalproject.service.IUserProfileService;

@RestController
@RequestMapping("/userprofile")
public class RegistrationConfirmationController {
	@Autowired
	private IUserProfileService service;


	@RequestMapping(value = "/regitrationConfirm", method = RequestMethod.GET)
	public ResponseEntity<Object> confirmRegistration(@RequestParam("token") String token) {

		VerificationToken verificationToken = service.getVerificationToken(token);
		if (verificationToken == null || !verificationToken.isValid()) {
			//throw the error
			return ResponseEntity.notFound().build();
		}else {

			UserProfile userProfile = service.getUserProfile(verificationToken.getToken());
			userProfile.setEnabled(true);
			userProfile.setStartMemberDate(LocalDateTime.now());
			verificationToken.setValid(false);
			service.saveRegisteredUser(userProfile,verificationToken);
			return ResponseEntity.ok(userProfile);
		}

	}
}