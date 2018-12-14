package com.cmpe275.finalproject.domain.users;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cmpe275.finalproject.domain.movieplayed.MoviePlayed;

public interface UserProfileRepository extends MongoRepository<UserProfile,String>{
	UserProfile findBy_id(ObjectId _id);
	UserProfile findByUsername(@Param("username") String username);
	
	@Query("{$expr:{$and:[{$eq:[{$year:'$startMemberDate'}, ?0]}, {$eq:[{$month:'$startMemberDate'}, ?1]}]}}")
	List<UserProfile> findAllUniqueUsersBy(int year, int month);
	
	@Query("{$expr:{$or:[{$and:[{$eq:[{$year:'$startMemberDate'}, ?0]}, {$gte:[{$month:'$startMemberDate'}, ?1]}]},{$and:[{$gt:[{$year:'$startMemberDate'}, ?0]}, {$lte:[{$month:'$startMemberDate'}, ?1]}]}]}}")
	List<UserProfile> findAllUniqueUsersByYearly(int lyear, int lmonth);
}
