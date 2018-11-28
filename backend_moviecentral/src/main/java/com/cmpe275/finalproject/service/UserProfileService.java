package com.cmpe275.finalproject.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;
import com.cmpe275.finalproject.domain.users.VerificationToken;
import com.cmpe275.finalproject.domain.users.VerificationTokenRepository;

@Service
@Transactional
public class UserProfileService implements IUserProfileService {

	@Autowired
	private UserProfileRepository repository;

	@Autowired
	private VerificationTokenRepository tokenRepository;



	@Override
	public UserProfile registerNewUserAccount(UserProfile userProfile) {
		// TODO Auto-generated method stub
		userProfile.set_id(ObjectId.get());
		return repository.save(userProfile);
	}

	@Override
	public UserProfile getUserProfile(String verificationToken) {
		// TODO Auto-generated method stub
		VerificationToken tkn = tokenRepository.findByToken(verificationToken);
		ObjectId userId = tkn.getUserId();

		return repository.findBy_id(userId);
	}

	@Override
	public void saveRegisteredUser(UserProfile userProfile) {
		repository.save(userProfile);

	}

	@Override
	public void createVerificationToken(UserProfile user, String token) {
		// TODO Auto-generated method stub
		VerificationToken myToken = new VerificationToken(null, token, user.get_id());
		myToken.set_id(ObjectId.get());
		tokenRepository.save(myToken);
	}

	@Override
	public VerificationToken getVerificationToken(String verificationToken) {
		// TODO Auto-generated method stub
		return tokenRepository.findByToken(verificationToken);
	}

}
