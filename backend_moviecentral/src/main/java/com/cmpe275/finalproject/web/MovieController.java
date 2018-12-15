package com.cmpe275.finalproject.web;


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

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.finalproject.domain.movie.Movie;
import com.cmpe275.finalproject.domain.movie.MovieDAL;
import com.cmpe275.finalproject.domain.movie.MovieDALImpl;
import com.cmpe275.finalproject.domain.movie.MovieRepository;
import com.cmpe275.finalproject.errorhandling.UserNotFoundException;
import org.springframework.http.ResponseEntity;

@RestController
public class MovieController {


	@Autowired
	MovieRepository repository;

	@Autowired
	MovieDAL movieDAL;

	@Bean
	public MovieDAL getMovieDAL() {
		return new MovieDALImpl();
	}

	@RequestMapping(value="/admin/movie",method = RequestMethod.POST)
	public Movie createProfile(HttpServletRequest request,@Valid @RequestBody Movie movie) {
		movie.set_id(ObjectId.get());
		movie.setNumberOfReviewers(0);
		movie.setNumberOfStars(0);
		repository.save(movie);
		return movie;
	}

	@RequestMapping(value="/movie/search", method = RequestMethod.POST)
	public Page<Movie> searchMovie(@RequestBody Map<String, Object> payload) {

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
		Map<String, Object> filters = (Map) payload.get("filters");
		return movieDAL.searchMovieByKeyWord(keywordList, pageable,filters);
	}
	
	@RequestMapping(value="/movie/getByRating", method = RequestMethod.POST)
	public Page<Movie> getMoviesByRating(@RequestBody Map<String, Object> payload) {

		int page = (Integer) payload.get("page");
		int size =   (Integer) payload.get("size");
		Pageable pageable = new PageRequest(page,size);
		//checking to see filter is applied
		Map<String, Object> filters = (Map) payload.get("filters");
		return movieDAL.getAllMoviesByRating( pageable);
	}

	@RequestMapping(value="/movie/{id}", method = RequestMethod.GET)
	public Movie getMovieById(@PathVariable("id") ObjectId _id) {
		Movie movie =  repository.findBy_id(_id);
		if(movie != null) return movie;
		throw new UserNotFoundException("id-" + _id);	
	}
	
	@RequestMapping(value="/admin/movie/{id}", method = RequestMethod.DELETE)
	public Movie deleteMovieById(@PathVariable("id") ObjectId _id) {
		Movie movie =  repository.findBy_id(_id);
		repository.deleteById(_id.toString());
		if(movie != null) return movie;
		throw new UserNotFoundException("id-" + _id);	
	}
	
	@RequestMapping(value="/admin/movie/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateMovie(@RequestBody Movie movie,@PathVariable("id") ObjectId _id) {
		Movie movieOptional = repository.findBy_id(_id);

		if (movieOptional == null)
			return ResponseEntity.notFound().build();

		movie.set_id(_id);
	
		repository.save(movie);

		return ResponseEntity.ok(movie);
	}
}
