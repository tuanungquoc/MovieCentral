package com.cmpe275.finalproject.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

import com.cmpe275.finalproject.domain.movieplayed.MoviePlayed;
import com.cmpe275.finalproject.domain.movieplayed.MoviePlayedRepository;
import com.cmpe275.finalproject.domain.order.Order;
import com.cmpe275.finalproject.domain.order.OrderRepository;
import com.cmpe275.finalproject.domain.order.OrderStats;
import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileDAL;
import com.cmpe275.finalproject.domain.users.UserProfileDALImpl;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;
import com.cmpe275.finalproject.domain.users.UserProfileStats;
import com.cmpe275.finalproject.domain.users.UserProfileTypeStats;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;

@RestController
@RequestMapping
public class AdminController {


	@Autowired
	UserProfileRepository repository;

	@Autowired
	OrderRepository orderRespository;

	@Autowired
	UserProfileDAL userProfileDAL;

	@Autowired
	MoviePlayedRepository moviePlayedRepository;


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

	@RequestMapping(value="/admin/userStats/monthly", method = RequestMethod.GET)
	public ResponseEntity<Object> getFinanceByMonth(@Valid @RequestParam("year") int year,@Valid @RequestParam("month") int month) {
		List stats = new ArrayList<UserProfileTypeStats>();
		//Subcribed User
		List<UserProfileStats> setSubcribedUsers = userProfileDAL.getUniqueSubcribedUsersByMonthAndYear(year,month);
		stats.add(new UserProfileTypeStats("Subcribed", setSubcribedUsers.size()));
		//PPV User
		List<Order> orders = orderRespository.findAllPPVBy(year, month);
		Set<String> setPPVUsers = new HashSet<String>(); 
		for(Order order : orders) {
			setPPVUsers.add(order.getUserId());
		}
		stats.add(new UserProfileTypeStats("PayPerView", setPPVUsers.size()));
		//Active User
		List<MoviePlayed> moviePlayedList = moviePlayedRepository.findAllActiveUsersBy(year, month);
		Set<String> setActiveUsers = new HashSet<String>(); 
		for(MoviePlayed moviePlayed : moviePlayedList) {
			setActiveUsers.add(moviePlayed.getCustomerId());
		}
		stats.add(new UserProfileTypeStats("Active", setActiveUsers.size()));
		//All unqiue user
		List<UserProfile> uniqueUserList = repository.findAllUniqueUsersBy(year, month);

		stats.add(new UserProfileTypeStats("Uniqued", uniqueUserList.size()));

		return ResponseEntity.ok(stats);
	}

	@RequestMapping(value="/admin/userStats/yearly", method = RequestMethod.GET)
	public ResponseEntity<Object> getFinanceByYear() {
		List stats = new ArrayList<UserProfileTypeStats>();

		int lmonth = LocalDateTime.now().minusMonths(12).getMonthValue();
		int lyear = LocalDateTime.now().minusMonths(12).getYear();
		List<UserProfileStats> setSubcribedUsers = userProfileDAL.getUniqueSubcribedUsersLast12Months(lyear, lmonth);
		stats.add(new UserProfileTypeStats("Subcribed", setSubcribedUsers.size()));

		//PPV User
		List<Order> orders = orderRespository.findAllPPVByYearly(lyear, lmonth);
		Set<String> setPPVUsers = new HashSet<String>(); 
		for(Order order : orders) {
			setPPVUsers.add(order.getUserId());
		}
		stats.add(new UserProfileTypeStats("PayPerView", setPPVUsers.size()));
		//Active User
		List<MoviePlayed> moviePlayedList = moviePlayedRepository.findAllActiveUsersByYearly(lmonth, lmonth);
		Set<String> setActiveUsers = new HashSet<String>(); 
		for(MoviePlayed moviePlayed : moviePlayedList) {
			setActiveUsers.add(moviePlayed.getCustomerId());
		}
		stats.add(new UserProfileTypeStats("Active", setActiveUsers.size()));
		//All unqiue user
		List<UserProfile> uniqueUserList = repository.findAllUniqueUsersByYearly(lyear, lmonth);

		stats.add(new UserProfileTypeStats("Uniqued", uniqueUserList.size()));

		return ResponseEntity.ok(stats);
	}


}
