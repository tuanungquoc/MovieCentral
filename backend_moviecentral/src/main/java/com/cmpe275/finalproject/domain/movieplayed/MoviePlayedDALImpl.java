package com.cmpe275.finalproject.domain.movieplayed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;


public class MoviePlayedDALImpl implements MoviePlayedDAL {


	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MoviePlayedStats> getStatsMoviePlayedByDays(int days) {
		// TODO Auto-generated method stub
		MatchOperation matchOperation = getMatchOperationByDays(days);
		GroupOperation groupOperation = getGroupOperation();
		ProjectionOperation projectionOperation = getProjectOperation();
		//  ProjectionOperation projectionOperation1 = getProjectOperation1();
		Sort sort = new Sort(Sort.Direction.DESC, "numberOfPlays");
		return mongoTemplate.aggregate(
				Aggregation.newAggregation(
						matchOperation,
						groupOperation,
						projectionOperation,
						Aggregation.sort(sort)),
				MoviePlayed.class,MoviePlayedStats.class).getMappedResults();
	}


	private MatchOperation getMatchOperationByDays( int days) {
		LocalDateTime timeFilter = LocalDateTime.now().minusDays(days);
		if(days > 1) {
		 timeFilter = timeFilter.toLocalDate().atStartOfDay();
		}
		Criteria priceCriteria = Criteria.where("timePlayed").gte(timeFilter);
		return Aggregation.match(priceCriteria);
	} 

	private GroupOperation getGroupOperation() {
		return Aggregation.group("movie.title","movie._id")
				.count().as("numberOfPlays");
	}

	private ProjectionOperation getProjectOperation() {
		return Aggregation.project("numberOfPlays")
				.andExpression("title").as("title")
				.andExpression("_id").as("_id");
	}



}
