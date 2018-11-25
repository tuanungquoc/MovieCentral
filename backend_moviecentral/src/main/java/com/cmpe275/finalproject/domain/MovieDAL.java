package com.cmpe275.finalproject.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieDAL {
	List<Movie> getAllMovies();
	Page<Movie> searchMovieByKeyWord(List<String>keywords,Pageable pageable);
}
