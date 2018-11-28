package com.cmpe275.finalproject.domain.users;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends MongoRepository<UserProfile,String>{
	UserProfile findBy_id(ObjectId _id);
	UserProfile findByUsername(@Param("username") String username);

}
