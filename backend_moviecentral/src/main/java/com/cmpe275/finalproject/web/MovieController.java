package com.cmpe275.finalproject.web;


import java.util.Arrays;
import java.util.List;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.cmpe275.finalproject.domain.Movie;
import com.cmpe275.finalproject.domain.MovieDAL;
import com.cmpe275.finalproject.domain.MovieDALImpl;
import com.cmpe275.finalproject.domain.MovieRepository;
import com.cmpe275.finalproject.domain.UserProfile;
import com.cmpe275.finalproject.domain.UserProfileRepository;
import com.cmpe275.finalproject.domain.VerificationToken;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;

@RestController
@RequestMapping("/movie")
public class MovieController {


	@Autowired
	MovieRepository repository;
	
	@Autowired
	MovieDAL movieDAL;

	@Bean
	public MovieDAL getMovieDAL() {
		return new MovieDALImpl();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Movie createProfile(HttpServletRequest request,@Valid @RequestBody Movie movie) {
		movie.set_id(ObjectId.get());
		repository.save(movie);
		return movie;
	}

	@RequestMapping(value="/search", method = RequestMethod.POST)
	public Page<Movie> searchMovie(@RequestBody Map<String, Object> payload) {
		String keyword = (String) payload.get("search");
		if(keyword == null) {
			keyword = "";
		}
		String[] keys = keyword.split(" ");
		int page = Integer.parseInt((String)payload.get("page"));
		int size =  Integer.parseInt((String)payload.get("size"));
		Pageable pageable = new PageRequest(page,size);
		return movieDAL.searchMovieByKeyWord(Arrays.asList(keys), pageable);
	}
	
}
