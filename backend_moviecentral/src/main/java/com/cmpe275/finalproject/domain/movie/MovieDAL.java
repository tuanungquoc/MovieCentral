package com.cmpe275.finalproject.domain.movie;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieDAL {
	List<Movie> getAllMovies();
	Page<Movie> searchMovieByKeyWord(List<String>keywords,Pageable pageable,Map<String,Object>filters);
}
