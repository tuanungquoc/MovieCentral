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
@RequestMapping("/api")
public class TestController {


	
	@RequestMapping(method = RequestMethod.GET)
	public String getProfileById() {
		return "hello";
	}
	
}
