package com.cmpe275.finalproject.domain.moviereview;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cmpe275.finalproject.domain.movie.Movie;

public interface MovieReviewDAL {
	List<MovieReviewStats> getStatsMovieReviewFrom(ObjectId moveId);
	Page<MovieReview> getMovieReviewsFrom(ObjectId movieId,Pageable pageable);
}
