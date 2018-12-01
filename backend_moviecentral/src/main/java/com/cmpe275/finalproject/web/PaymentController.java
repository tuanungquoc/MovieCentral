package com.cmpe275.finalproject.web;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;
import com.cmpe275.finalproject.order.Order;
import com.cmpe275.finalproject.order.OrderDAL;
import com.cmpe275.finalproject.order.OrderDALImpl;
import com.cmpe275.finalproject.order.OrderRepository;
import com.cmpe275.finalproject.order.OrderStats;
import com.cmpe275.finalproject.order.PaymentInfoRepository;

import org.springframework.http.ResponseEntity;

@RestController
public class PaymentController {

	@Autowired
	OrderRepository repository;

	@Autowired
	PaymentInfoRepository paymentInfoRepository;

	@Autowired
	OrderDAL orderDAL;

	@Autowired
	UserProfileRepository userRepository;

	@Bean
	public OrderDAL getOrderDAL() {
		return new OrderDALImpl();
	}

	@RequestMapping(value="/subcribe",method = RequestMethod.POST)
	public ResponseEntity<Object> subcribeControl(@Valid @RequestBody Order order) {
		if(order.getQuantity() != 0) {
			//Save payment

			ObjectId cusomterId = order.getUserId();
			UserProfile user = userRepository.findBy_id(cusomterId);
			if(user!=null) {
				LocalTime midnight = LocalTime.MIDNIGHT;
				LocalDate today = LocalDate.now();
				LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
				if(user.getNextRenewalDate() == null ) {
					user.setSubcribed(true);
					user.setNextRenewalDate(todayMidnight.plusMonths(order.getQuantity()));
					//user.setNextRenewalDate(LocalDateTime.now());

				}else {
					//				    ZoneId utcZ = ZoneId.of("Z");
					//					ZonedDateTime nextRenewalDateInZ = ZonedDateTime.of(user.getNextRenewalDate(),utcZ);
					//					LocalDateTime nextRenewalDateLocal = nextRenewalDateInZ.toLocalDateTime();
					LocalDateTime nextRenewalDateLocal = user.getNextRenewalDate();
					
					if(nextRenewalDateLocal.isBefore(todayMidnight)) {
						user.setNextRenewalDate(todayMidnight.plusMonths(order.getQuantity()));
					}else {
						user.setNextRenewalDate(nextRenewalDateLocal.plusMonths(order.getQuantity()));
					}
				}
				userRepository.save(user);
				//now save the payment
				order.setCreated(LocalDateTime.now());
				repository.save(order);
				return ResponseEntity.ok(order);
			}else {
				return ResponseEntity.badRequest().body("Bad request");
			}
		}else {
			return ResponseEntity.badRequest().body("Bad request");
		}
	}

	//	@RequestMapping(value="/review/retrieve", method = RequestMethod.POST)
	//	public Page<MovieReview> searchMovie(@RequestBody Map<String, Object> payload) {
	//
	//		ObjectId movieId = new ObjectId((String)payload.get("movieId"));
	//		int page = (Integer) payload.get("page");
	//		int size =   (Integer) payload.get("size");
	//		Pageable pageable = new PageRequest(page,size);
	//		//checking to see filter is applied
	//		return movieReviewDAL.getMovieReviewsFrom(movieId,pageable);				
	//	}
	//
	@RequestMapping(value="/admin/finance/monthly", method = RequestMethod.GET)
	public ResponseEntity<Object> getFinanceByMonth(@Valid @RequestParam("year") int year,@Valid @RequestParam("month") int month) {
//		int month = LocalDateTime.now().getMonthValue();
//		int year = LocalDateTime.now().getYear();
		List<OrderStats> orders = orderDAL.getStatsFinanceByMonthAndYear(year,month);
				//findByDateBetween(LocalDateTime.now().getYear(),month);
		
	
		return ResponseEntity.ok(orders);
	}
	
	@RequestMapping(value="/admin/finance/yearly", method = RequestMethod.GET)
	public ResponseEntity<Object> getFinanceByMonth() {
		int month = LocalDateTime.now().getMonthValue();
		int year = LocalDateTime.now().getYear();
		List<OrderStats> orders = orderDAL.getStatsFinanceByMonthAndYear(year,month);
				//findByDateBetween(LocalDateTime.now().getYear(),month);
		
	
		return ResponseEntity.ok(orders);
	}
}
