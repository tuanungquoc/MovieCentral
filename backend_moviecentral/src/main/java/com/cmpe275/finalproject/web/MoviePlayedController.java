package com.cmpe275.finalproject.web;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.finalproject.domain.movie.Movie;
import com.cmpe275.finalproject.domain.movie.MovieDAL;
import com.cmpe275.finalproject.domain.movie.MovieDALImpl;
import com.cmpe275.finalproject.domain.movie.MovieRepository;
import com.cmpe275.finalproject.domain.movieplayed.MoviePlayed;
import com.cmpe275.finalproject.domain.movieplayed.MoviePlayedBadRequest;
import com.cmpe275.finalproject.domain.movieplayed.MoviePlayedDAL;
import com.cmpe275.finalproject.domain.movieplayed.MoviePlayedDALImpl;
import com.cmpe275.finalproject.domain.movieplayed.MoviePlayedRepository;
import com.cmpe275.finalproject.domain.movieplayed.MoviePlayedStats;
import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;
import org.springframework.http.ResponseEntity;

@RestController
public class MoviePlayedController {


	@Autowired
	MovieRepository movieRepository;

	@Autowired
	UserProfileRepository userRepository;
	
	@Autowired
	MoviePlayedRepository moviePlayedRepository;
	
	@Autowired
	MoviePlayedDAL moviePlayedDAL;
	
	@Bean
	public MoviePlayedDAL getMoviePlayedDAL() {
		return new MoviePlayedDALImpl();
	}
	private void trackMoviePlayed(MoviePlayed moviePlayed){
		//find all playedRecord that is less than 24hrs
		MoviePlayed playedRecord = moviePlayedRepository.findByMovieIdAndCustomerIdWithTimePlayed(
				new ObjectId(moviePlayed.getMovie().get_id()),
				new ObjectId(moviePlayed.getCustomerId()),
				LocalDateTime.now().minusDays(1));
		if(playedRecord == null) {
			//save to DB
			moviePlayed.setTimePlayed(LocalDateTime.now());
			moviePlayedRepository.save(moviePlayed);
	}
		//else {
//			//checking if it has been played within 24 hours or not
//			LocalDateTime moviePlayedTime = playedRecord.getTimePlayed();
//			LocalDateTime next24hrs = moviePlayedTime.plusHours(24);
//			LocalDateTime now = LocalDateTime.now();
//			if(next24hrs.compareTo(now) > 0) {
//				//do nothing since it is still within 24 hrs
//			}else {
//				//saveTime
//				playedRecord.set_id(new ObjectId());
//				playedRecord.setTimePlayed(LocalDateTime.now());
//				moviePlayedRepository.save(playedRecord);
//			}
//		}
	}

	@RequestMapping(value="/movie/play",method = RequestMethod.POST)
	public ResponseEntity<Object> createProfile(HttpServletRequest request,@RequestBody Map<String, Object> payload) {
		//get movieId string
		String movieId = (String) payload.get("movieId");
		String customerId = (String) payload.get("customerId");

		if(movieId == null || customerId == null) {
			return ResponseEntity.notFound().build();
		}
		
		Movie movie = movieRepository.findBy_id(new ObjectId(movieId));
		UserProfile user = userRepository.findBy_id(new ObjectId(customerId));
		if(movie == null || user == null)
		{
			return ResponseEntity.notFound().build();
		}
		
		if(user.getRole().equals("ADMIN")) {
			return ResponseEntity.ok().build();
		}
		MoviePlayed moviePlayed = new MoviePlayed();
		moviePlayed.set_id(new ObjectId());
		moviePlayed.setMovie(movie);
		moviePlayed.setMovieAvailability(movie.getAvailability());
		moviePlayed.setCustomerId(new ObjectId(customerId));
		//checking eligibility
		if(movie.getAvailability().equals("Free")) {
			//return 200 and play
			trackMoviePlayed(moviePlayed);
			return ResponseEntity.ok().build();
		}else if(movie.getAvailability().equals("Subcribed")) {
			if(moviePlayed.getOrderId() != null) {
				trackMoviePlayed(moviePlayed);
				return ResponseEntity.ok().build();
			}
			//check if the user is still subribal
			if(user.getNextRenewalDate() == null || user.getNextRenewalDate().compareTo(LocalDateTime.now()) < 0) {
				//user need to pay for subcription
				MoviePlayedBadRequest badRequest = new MoviePlayedBadRequest();
				badRequest.setMovieId(movie.get_id());
				badRequest.setTotal(10);
				badRequest.setSubribed(false);
				badRequest.setTypeOfMovie(movie.getAvailability());
				return ResponseEntity.badRequest().body(badRequest);
			}
			trackMoviePlayed(moviePlayed);
			return ResponseEntity.ok().build();
		}else if(movie.getAvailability().equals("PayPerView")) {
			if(moviePlayed.getOrderId() != null) {
				trackMoviePlayed(moviePlayed);
				return ResponseEntity.ok().build();
			}
			MoviePlayedBadRequest badRequest = new MoviePlayedBadRequest();
			badRequest.setMovieId(movie.get_id());
			//regular user paying full price
			if(user.getNextRenewalDate() == null )
				badRequest.setTotal(movie.getPrice());
			else {
				if(user.getNextRenewalDate().compareTo(LocalDateTime.now()) < 0) {
					badRequest.setTotal(movie.getPrice());
				}else {
					badRequest.setTotal(movie.getPrice() / 2);
				}
			}
			badRequest.setTotal(movie.getPrice());
			badRequest.setSubribed(false);
			badRequest.setTypeOfMovie(movie.getAvailability());
			return ResponseEntity.badRequest().body(badRequest);
		}else {
			trackMoviePlayed(moviePlayed);
			return ResponseEntity.ok().build();
		}
	}
	
	@RequestMapping(value="/movie/play/byUser/{userId}", method = RequestMethod.GET)
	public List<MoviePlayed> getHistoryMoviePlayed(@PathVariable("userId") ObjectId userId) {

		Sort sort = new Sort(Sort.Direction.DESC, "timePlayed");
		return moviePlayedRepository.findAllMoviePlayedBy(userId, sort);

	}
	
	@RequestMapping(value="/movie/play/stats/{days}", method = RequestMethod.GET)
	public List<MoviePlayedStats> getMoviePlayedStats(@PathVariable("days") int days) {
		return moviePlayedDAL.getStatsMoviePlayedByDays(days);
		

	}
}
