package com.cmpe275.finalproject.domain.order;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;


public class OrderDALImpl implements OrderDAL {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<OrderStats> getStatsFinanceByMonthAndYear(int year, int month) {
		// TODO Auto-generated method stub
		MatchOperation matchOperation = getMatchOperationByMonthAndYear(year, month);
		GroupOperation groupOperation = getGroupOperation();
		ProjectionOperation projectionOperation = getProjectOperation();
		ProjectionOperation projectionOperation1 = getProjectOperation1();

		return mongoTemplate.aggregate(
				Aggregation.newAggregation(projectionOperation1,matchOperation,groupOperation,projectionOperation),
				Order.class,OrderStats.class).getMappedResults();
	}

	@Override
	public List<OrderStats> getStatsFinanceByLast12Months(int lyear, int lmonth) {
		MatchOperation matchOperation = getMatchOperationByLast12Months(lyear, lmonth);
		GroupOperation groupOperation = getGroupOperation();
		ProjectionOperation projectionOperation = getProjectOperation();
		ProjectionOperation projectionOperation1 = getProjectOperation1();

		return mongoTemplate.aggregate(
				Aggregation.newAggregation(projectionOperation1,matchOperation,groupOperation,projectionOperation),
				Order.class,OrderStats.class).getMappedResults();
	}

	private MatchOperation getMatchOperationByMonthAndYear(int year, int month) {
		Criteria priceCriteria = Criteria.where("year").is(year).and("month").is(month);
		return Aggregation.match(priceCriteria);
	} 

	private MatchOperation getMatchOperationByLast12Months(int lastYear, int lastYearMonth) {
		Criteria priceCriteria1 = Criteria.where("year").gte(lastYear).and("month").gte(lastYearMonth);
		Criteria priceCriteria2 = Criteria.where("year").gt(lastYear).and("month").lte(lastYearMonth);
		Criteria orCriteria = new Criteria().orOperator(priceCriteria1, priceCriteria2);
		return Aggregation.match(orCriteria);
	} 

	private GroupOperation getGroupOperation() {
		return Aggregation.group("typeOfPayment")
				.sum("total").as("sum");
	}

	private ProjectionOperation getProjectOperation() {
		return Aggregation.project("sum")
				.and("typeOfPayment").previousOperation();
	}

	private ProjectionOperation getProjectOperation1() {
		return Aggregation.project("_id").andExpression("month(created)").as("month")
				.andExpression("year(created)").as("year")
				.andExpression("typeOfPayment").as("typeOfPayment")
				.andExpression("total").as("total");

	}

	
}
