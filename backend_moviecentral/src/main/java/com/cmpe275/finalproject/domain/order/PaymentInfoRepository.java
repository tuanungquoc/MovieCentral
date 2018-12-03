package com.cmpe275.finalproject.domain.order;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentInfoRepository extends MongoRepository<PaymentInfo, String> {

}
