package com.cmpe275.finalproject.order;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface OrderRepository extends MongoRepository<Order, String>{
	@Query("{$expr:{$and:[{$eq:[{$year:'$created'}, ?0]}, {$eq:[{$month:'$created'}, ?1]}]}}")
	List<Order> findByDateBetween(int yeah, int month);
}
