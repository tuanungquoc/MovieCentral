package com.cmpe275.finalproject.domain.moviereview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.cmpe275.finalproject.domain.movie.Movie;

public class MovieReviewDALImpl implements MovieReviewDAL {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MovieReviewStats> getStatsMovieReviewFrom(ObjectId moveId) {
		// TODO Auto-generated method stub
		MatchOperation matchOperation = getMatchOperation(moveId);
	    GroupOperation groupOperation = getGroupOperation();
	    ProjectionOperation projectionOperation = getProjectOperation();
	    
	    return mongoTemplate.aggregate(
	    		Aggregation.newAggregation(matchOperation,groupOperation,projectionOperation),
	    		MovieReview.class,MovieReviewStats.class).getMappedResults();
	}

	@Override
	public Page<MovieReview> getMovieReviewsFrom(ObjectId movieId, Pageable pageable) {
		// TODO Auto-generated method stub
		String queryStr = "{'movieId':ObjectId('" + movieId + "')}";
		
		BasicQuery query = 
				new BasicQuery(queryStr);
		query.with(pageable);
		Page<MovieReview> movieReviewsPage = PageableExecutionUtils.getPage(
				mongoTemplate.find(query, MovieReview.class),
				pageable,
				() -> mongoTemplate.count(query, MovieReview.class));

		return movieReviewsPage;
	}
	
	private MatchOperation getMatchOperation(ObjectId movieId) {
	    Criteria priceCriteria = Criteria.where("movieId").is(movieId);
	    return Aggregation.match(priceCriteria);
	} 
	
	private GroupOperation getGroupOperation() {
	    return Aggregation.group("reviewRate")
	    		.count().as("totalPerRateReview");
	}
	
	private ProjectionOperation getProjectOperation() {
	    return Aggregation.project("totalPerRateReview")
	        .and("reviewRate").previousOperation();
	}
	
}
