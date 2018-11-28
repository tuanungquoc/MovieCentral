package com.cmpe275.finalproject.domain.moviereview;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cmpe275.finalproject.domain.movie.Movie;

public interface MovieReviewRepository extends MongoRepository<MovieReview,String>{
}
