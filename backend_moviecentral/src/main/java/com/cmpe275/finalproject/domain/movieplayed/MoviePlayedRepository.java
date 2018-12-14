package com.cmpe275.finalproject.domain.movieplayed;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cmpe275.finalproject.domain.movie.Movie;
import com.cmpe275.finalproject.domain.order.Order;

public interface MoviePlayedRepository extends MongoRepository<MoviePlayed, String> {
	 
	@Query(value = "{ 'customerId' : ?1, 'movie._id' : ?0 ,'timePlayed': {'$gte':?2}}")
	 MoviePlayed findByMovieIdAndCustomerIdWithTimePlayed(ObjectId movieId, ObjectId customerId,LocalDateTime time);
	
	@Query("{ 'customerId' : ?0}")
	List<MoviePlayed> findAllMoviePlayedBy(ObjectId customerId, Sort sort);
	
	@Query("{$expr:{$and:[{$eq:[{$year:'$timePlayed'}, ?0]}, {$eq:[{$month:'$timePlayed'}, ?1]}]}}")
	List<MoviePlayed> findAllActiveUsersBy(int year, int month);
	
	@Query("{$expr:{$or:[{$and:[{$eq:[{$year:'$timePlayed'}, ?0]}, {$gte:[{$month:'$timePlayed'}, ?1]}]},{$and:[{$gt:[{$year:'$timePlayed'}, ?0]}, {$lte:[{$month:'$timePlayed'}, ?1]}]}]}}")
	List<MoviePlayed> findAllActiveUsersByYearly(int lyear, int lmonth);
	
}
