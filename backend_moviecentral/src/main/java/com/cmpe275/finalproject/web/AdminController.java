package com.cmpe275.finalproject.web;

import java.time.LocalDateTime;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.finalproject.domain.order.OrderStats;
import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileDAL;
import com.cmpe275.finalproject.domain.users.UserProfileDALImpl;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;
import com.cmpe275.finalproject.domain.users.UserProfileStats;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;

@RestController
@RequestMapping
public class AdminController {


	@Autowired
	UserProfileRepository repository;
	
	
	@Autowired
	UserProfileDAL userProfileDAL;

	@Bean
	public UserProfileDAL getUserProfileDAL1() {
		return new UserProfileDALImpl();
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
	
	@RequestMapping(value="/admin/subcribedUser/monthly", method = RequestMethod.GET)
	public ResponseEntity<Object> getFinanceByMonth(@Valid @RequestParam("year") int year,@Valid @RequestParam("month") int month) {
		List<UserProfileStats> profileStats = userProfileDAL.getUniqueSubcribedUsersByMonthAndYear(year,month);	
		return ResponseEntity.ok(profileStats);
	}
	
}
