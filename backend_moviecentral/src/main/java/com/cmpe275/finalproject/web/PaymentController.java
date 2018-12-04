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

import com.cmpe275.finalproject.domain.movie.Movie;
import com.cmpe275.finalproject.domain.movie.MovieRepository;
import com.cmpe275.finalproject.domain.order.Order;
import com.cmpe275.finalproject.domain.order.OrderDAL;
import com.cmpe275.finalproject.domain.order.OrderDALImpl;
import com.cmpe275.finalproject.domain.order.OrderReponse;
import com.cmpe275.finalproject.domain.order.OrderRepository;
import com.cmpe275.finalproject.domain.order.OrderStats;
import com.cmpe275.finalproject.domain.order.PaymentInfoRepository;
import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;

import org.springframework.http.ResponseEntity;

@RestController
public class PaymentController {

	@Autowired
	OrderRepository repository;

	@Autowired
	PaymentInfoRepository paymentInfoRepository;

	@Autowired
	MovieRepository movieRepository;
	
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
			ObjectId cusomterId = new ObjectId(order.getUserId());
			UserProfile user = userRepository.findBy_id(cusomterId);
			if(user!=null) {
				LocalTime midnight = LocalTime.MIDNIGHT.plusHours(23).plusMinutes(59);
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
				
				//now save the payment
				order.setCreated(LocalDateTime.now());
				repository.save(order);
				userRepository.save(user);
				OrderReponse orderResp = new OrderReponse();
				orderResp.setOrderId(order.get_id());
				orderResp.setTypeOfPayment(order.getTypeOfPayment());
				orderResp.setUserId(user.get_id());
				return ResponseEntity.ok(orderResp);
			}else {
				return ResponseEntity.badRequest().body("Bad request");
			}
		}else {
			return ResponseEntity.badRequest().body("Bad request");
		}
	}
	
	@RequestMapping(value="/moviepay",method = RequestMethod.POST)
	public ResponseEntity<Object> moviePayControl(@Valid @RequestBody Order order) {
		if(order.getQuantity() != 0 || order.getMovieId() == null) {
			//Save payment
			ObjectId cusomterId = new ObjectId(order.getUserId());
			UserProfile user = userRepository.findBy_id(cusomterId);
			Movie movie = movieRepository.findBy_id(new ObjectId(order.getMovieId()));

			if(movie!=null && movie != null && movie.getAvailability().equals("PayPerView")) {
				//now save the payment
				order.setCreated(LocalDateTime.now());
				repository.save(order);
				userRepository.save(user);
				OrderReponse orderResp = new OrderReponse();
				orderResp.setOrderId(order.get_id());
				orderResp.setTypeOfPayment(order.getTypeOfPayment());
				orderResp.setUserId(user.get_id());
				orderResp.setMovieId(movie.get_id());
				return ResponseEntity.ok(orderResp);
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
		int cmonth = LocalDateTime.now().getMonthValue();
		int cyear = LocalDateTime.now().getYear();
		int lmonth = LocalDateTime.now().minusMonths(12).getMonthValue();
		int lyear = LocalDateTime.now().minusMonths(12).getYear();
		List<OrderStats> orders = orderDAL.getStatsFinanceByLast12Months(lyear,lmonth);
				//findByDateBetween(LocalDateTime.now().getYear(),month);
		
	
		return ResponseEntity.ok(orders);
	}
}
