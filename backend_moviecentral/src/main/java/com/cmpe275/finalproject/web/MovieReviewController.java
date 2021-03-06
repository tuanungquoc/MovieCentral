package com.cmpe275.finalproject.web;


import java.util.Arrays;
import java.util.Date;
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

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.finalproject.domain.movie.Movie;
import com.cmpe275.finalproject.domain.movie.MovieDAL;
import com.cmpe275.finalproject.domain.movie.MovieDALImpl;
import com.cmpe275.finalproject.domain.movie.MovieRepository;
import com.cmpe275.finalproject.domain.moviereview.MovieReview;
import com.cmpe275.finalproject.domain.moviereview.MovieReviewDAL;
import com.cmpe275.finalproject.domain.moviereview.MovieReviewDALImpl;
import com.cmpe275.finalproject.domain.moviereview.MovieReviewRepository;
import com.cmpe275.finalproject.domain.moviereview.MovieReviewStats;
import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;
import org.springframework.http.ResponseEntity;


@RestController
public class MovieReviewController {

	@Autowired
	MovieReviewRepository repository;

	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	UserProfileRepository userProfileRepository;
	
	@Autowired
	MovieReviewDAL movieReviewDAL;

	@Bean
	public MovieReviewDAL getMovieReviewDAL() {
		return new MovieReviewDALImpl();
	}

	@RequestMapping(value="/review",method = RequestMethod.POST)
	public ResponseEntity<Object> createReview(@RequestBody MovieReview review) {
		//checking to see if user already posted a review
		MovieReview existingReview = repository.findByCustomerIdAndMovieId(new ObjectId(review.getCustomerId()),
				new ObjectId(review.getMovieId()));
		if(existingReview != null ) {
			return ResponseEntity.badRequest().body("{\"error\":\"Bad Request\",\"status\":400}");
			
		}else {
			//finding customer name
			UserProfile profile = userProfileRepository.findBy_id(new ObjectId(review.getCustomerId()));
			review.set_id(ObjectId.get());
			review.setCreated(new Date());
			review.setProfileName(profile.getProfileName());
			repository.save(review);
			// update numberOfStar of the movie
			Movie movie = movieRepository.findBy_id(new ObjectId(review.getMovieId()));
			int numberOfReviewer = movie.getNumberOfReviewers();
			float numberOfStars = movie.getNumberOfStars();
			movie.setNumberOfReviewers(numberOfReviewer +1);
			float ave = ((numberOfStars * numberOfReviewer) + review.getReviewRate())/(numberOfReviewer + 1);
			movie.setNumberOfStars((int)ave);
			movieRepository.save(movie);
			
			return ResponseEntity.ok().body(review);
			
		}
	}

	@RequestMapping(value="/review/retrieve", method = RequestMethod.POST)
	public Page<MovieReview> searchMovie(@RequestBody Map<String, Object> payload) {

		ObjectId movieId = new ObjectId((String)payload.get("movieId"));
		int page = (Integer) payload.get("page");
		int size =   (Integer) payload.get("size");
		Pageable pageable = new PageRequest(page,size);
		//checking to see filter is applied
		return movieReviewDAL.getMovieReviewsFrom(movieId,pageable);				
	}

	@RequestMapping(value="/stats/review/{movieId}", method = RequestMethod.GET)
	public List<MovieReviewStats> getMovieById(@PathVariable("movieId") ObjectId movieId) {
		return movieReviewDAL.getStatsMovieReviewFrom(movieId);
	}
}
