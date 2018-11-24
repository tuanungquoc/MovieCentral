package com.cmpe275.finalproject.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.cmpe275.finalproject.domain.UserProfile;
import com.cmpe275.finalproject.domain.UserProfileRepository;
import com.cmpe275.finalproject.domain.VerificationToken;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;

@RestController
@RequestMapping("/userprofile")
public class UserProfileController {


	@Autowired
	UserProfileRepository repository;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;

	@RequestMapping(method = RequestMethod.POST)
	public UserProfile createProfile(HttpServletRequest request,@Valid @RequestBody UserProfile profile) {
		profile.set_id(ObjectId.get());
	
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent
				(profile, request.getLocale(), appUrl));
	
		return profile;
	}

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public UserProfile getProfileById(@PathVariable("id") ObjectId _id) {

		UserProfile userProfile =  repository.findBy_id(_id);
		System.out.println(userProfile);
		if(userProfile != null) return userProfile;
		throw new UserNotFoundException("id-" + _id);	
	}
	
}
