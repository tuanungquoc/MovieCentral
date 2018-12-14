package com.cmpe275.finalproject.domain.order;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cmpe275.finalproject.domain.moviereview.MovieReview;

public interface OrderRepository extends MongoRepository<Order, String>{
	@Query("{$expr:{$and:[{$eq:[{$year:'$created'}, ?0]}, {$eq:[{$month:'$created'}, ?1]}]}}")
	List<Order> findByDateBetween(int yeah, int month);
	
	@Query("{'typeOfPayment': 'PayPerView', $expr:{$and:[{$eq:[{$year:'$created'}, ?0]}, {$eq:[{$month:'$created'}, ?1]}]}}")
	List<Order> findAllPPVBy(int year, int month);
	
	@Query("{'typeOfPayment': 'PayPerView', $expr:{$or:[{$and:[{$eq:[{$year:'$created'}, ?0]}, {$gte:[{$month:'$created'}, ?1]}]},{$and:[{$gt:[{$year:'$created'}, ?0]}, {$lte:[{$month:'$created'}, ?1]}]}]}}")
	List<Order> findAllPPVByYearly(int lyear, int lmonth);
	
	Order findByUserIdAndMovieId(@Param("userId") ObjectId userId,@Param("movieId") ObjectId movieId);
}
