package com.cmpe275.finalproject.web;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileDAL;
import com.cmpe275.finalproject.domain.users.UserProfileDALImpl;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;

@RestController
@RequestMapping("/userprofile")
public class UserProfileController {


	@Autowired
	UserProfileRepository repository;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Autowired
	UserProfileDAL userProfileDAL;

	@Bean
	public UserProfileDAL getUserProfileDAL() {
		return new UserProfileDALImpl();
	}


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
	
	@RequestMapping(value="/admin/search", method = RequestMethod.POST)
	public Page<UserProfile> searchUser(@RequestBody Map<String, Object> payload) {

		String keyword = (String) payload.get("search");
		List<String> keywordList;
		if(keyword == null) {
			keywordList = null;
		}
		else {
			String[] keys = keyword.split(" ");
			keywordList = Arrays.asList(keys);
		}
		int page = (Integer) payload.get("page");
		int size =   (Integer) payload.get("size");
		Pageable pageable = new PageRequest(page,size);
		//checking to see filter is applied
		
		return userProfileDAL.searchProfileByKeyWord(keywordList, pageable);
	}
}
