package com.cmpe275.finalproject.service;

import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.VerificationToken;

public interface IUserProfileService {
	
	UserProfile registerNewUserAccount(UserProfile userProfile);
	
	UserProfile getUserProfile(String verificationToken);

	void saveRegisteredUser(UserProfile userProfile,VerificationToken token);
	
	void createVerificationToken(UserProfile user, String token);

	VerificationToken getVerificationToken(String verificationToken);
	
}