package com.cmpe275.finalproject.service;

import com.cmpe275.finalproject.domain.UserProfile;
import com.cmpe275.finalproject.domain.VerificationToken;

public interface IUserProfileService {
	
	UserProfile registerNewUserAccount(UserProfile userProfile);
	
	UserProfile getUserProfile(String verificationToken);

	void saveRegisteredUser(UserProfile userProfile);
	
	void createVerificationToken(UserProfile user, String token);

	VerificationToken getVerificationToken(String verificationToken);
	
}