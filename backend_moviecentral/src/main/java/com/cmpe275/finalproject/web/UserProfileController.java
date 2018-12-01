package com.cmpe275.finalproject.web;

import java.util.Date;
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

import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;
import com.cmpe275.finalproject.domain.users.VerificationToken;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;
import com.cmpe275.finalproject.order.Order;

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
		if(profile.getUsername().contains("sjsu.edu")) {
			profile.setRole("ADMIN");
		}
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
	
//	@RequestMapping(value="/subcribed",method = RequestMethod.POST)
//	public UserProfile subcribeProfile(@RequestBody SubcribedPayment payment) {
//		UserProfile profile = repository.findBy_id(payment.getUserId());
//		if(profile != null) {
//			int totalMonths = payment.getTotal() / 10;
//			if(totalMonths > 0) {
//				if(profile.getNextRenewalDate() == null) {
//					Date currentDate = new Date();
//					currentDate.a
//				}
//			}
//		}
//		return profile;
//	}
}
