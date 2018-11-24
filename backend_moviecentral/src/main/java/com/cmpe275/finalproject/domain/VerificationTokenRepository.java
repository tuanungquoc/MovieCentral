package com.cmpe275.finalproject.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken,String>{
	VerificationToken findBy_id(ObjectId _id);
	VerificationToken findByToken(@Param("token") String token);
	
}
