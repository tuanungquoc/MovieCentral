package com.cmpe275.finalproject.domain.movieplayed;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cmpe275.finalproject.domain.movie.Movie;

public interface MoviePlayedRepository extends MongoRepository<MoviePlayed, String> {
	 
	@Query(value = "{ 'customerId' : ?1, 'movie._id' : ?0 ,'timePlayed': {'$gte':?2}}")
	 MoviePlayed findByMovieIdAndCustomerIdWithTimePlayed(ObjectId movieId, ObjectId customerId,LocalDateTime time);
	
	@Query("{ 'customerId' : ?0}")
	List<MoviePlayed> findAllMoviePlayedBy(ObjectId customerId, Sort sort);
}
