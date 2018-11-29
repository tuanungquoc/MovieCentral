package com.cmpe275.finalproject.domain.moviereview;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;



public interface MovieReviewRepository extends MongoRepository<MovieReview,String>{
	
	MovieReview findByCustomerIdAndMovieId(
			@Param("customerId") ObjectId customerId,@Param("movieId") ObjectId movieId);
	
}
