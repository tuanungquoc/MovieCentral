package com.cmpe275.finalproject.domain.users;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

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
import com.cmpe275.finalproject.domain.order.Order;
import com.cmpe275.finalproject.domain.order.OrderStats;


public class UserProfileDALImpl implements UserProfileDAL{

	@Autowired
	private MongoTemplate mongoTemplate;

	
	@Override
	public Page<UserProfile> searchProfileByKeyWord(List<String> keywords, Pageable pageable) {
		// TODO Auto-generated method stub
		ArrayList querySearchFilter = new ArrayList<String>();
		//building search query

		List<String> compiledSearchQueryStr = new ArrayList();
		if(keywords != null) {
			for(String keyword: keywords) {
				compiledSearchQueryStr.add( "{\"profileName\": {'$regex' : '" + keyword + "'" + "," +"'$options': 'i'" + "} }");
			}
		}


		String searchQuery = "{$or:" + compiledSearchQueryStr.toString() + "}";

		String finalQuery;

		if(compiledSearchQueryStr.size() == 0) {
			finalQuery = "{}";
		}else{
			finalQuery = searchQuery;
		}


		BasicQuery query = 
				new BasicQuery(finalQuery);
		query.with(pageable);
		Page<UserProfile> profilePage = PageableExecutionUtils.getPage(
				mongoTemplate.find(query, UserProfile.class),
				pageable,
				() -> mongoTemplate.count(query, UserProfile.class));

		return profilePage;
	}

	
	@Override
	public List<UserProfileStats> getUniqueSubcribedUsersByMonthAndYear(int year, int month) {
		// TODO Auto-generated method stub
		MatchOperation matchOperation = getMatchOperationByMonthAndYear(year, month);
	    GroupOperation groupOperation = getGroupOperation();
	    ProjectionOperation projectionOperation = getProjectOperation();
	    ProjectionOperation projectionOperation1 = getProjectOperation1();

	    return mongoTemplate.aggregate(
	    		Aggregation.newAggregation(projectionOperation1,matchOperation,groupOperation,projectionOperation),
	    		UserProfile.class,UserProfileStats.class).getMappedResults();
	}
	
	@Override
	public List<UserProfileStats> getUniqueSubcribedUsersLast12Months(int lyear, int lmonth) {
		MatchOperation matchOperation = getMatchOperationByLast12Months(lyear, lmonth);
	    GroupOperation groupOperation = getGroupOperation();
	    ProjectionOperation projectionOperation = getProjectOperation();
	    ProjectionOperation projectionOperation1 = getProjectOperation1();

	    return mongoTemplate.aggregate(
	    		Aggregation.newAggregation(projectionOperation1,matchOperation,groupOperation,projectionOperation),
	    		UserProfile.class,UserProfileStats.class).getMappedResults();
	}
	
	private MatchOperation getMatchOperationByMonthAndYear(int year, int month) {
	    //check next renewal date
	    Criteria priceCriteria1 = Criteria.where("year").is(year).and("month").gte(month);
	    Criteria priceCriteria2 = Criteria.where("year").gt(year);
	    
	    //check start subcribing date to make sure the member subcribed on the month you look at
	    Criteria priceCriteria4 = Criteria.where("startSubcribedYear").is(year).and("startSubcribedMonth").lte(month);
	    Criteria priceCriteria5 = Criteria.where("startSubcribedYear").lt(year);
	    
	    
	    Criteria priceCriteria3 = Criteria.where("isSubcribed").is(true);

	    Criteria orCriteria1 = new Criteria().orOperator(priceCriteria1, priceCriteria2);
	    Criteria orCriteria2 = new Criteria().orOperator(priceCriteria4, priceCriteria5);

	    Criteria andCriteria = new Criteria().andOperator(priceCriteria3, orCriteria1, orCriteria2);
	    return Aggregation.match(andCriteria);
	} 
	
	private MatchOperation getMatchOperationByLast12Months(int lastYear, int lastYearMonth) {
		Criteria priceCriteria1 = Criteria.where("startSubcribedYear").gte(lastYear).and("startSubcribedMonth").gte(lastYearMonth);
	    Criteria priceCriteria2 = Criteria.where("startSubcribedYear").gt(lastYear).and("startSubcribedMonth").lte(lastYearMonth);
	    
	    Criteria priceCriteria4 = Criteria.where("startSubcribedYear").lte(lastYear + 1).and("startSubcribedMonth").lte(lastYearMonth);
	    Criteria priceCriteria5 = Criteria.where("startSubcribedYear").lt(lastYear + 1).and("startSubcribedMonth").gte(lastYearMonth);
	    
	    
	    Criteria priceCriteria3 = Criteria.where("isSubcribed").is(true);

	    Criteria orCriteria1 = new Criteria().orOperator(priceCriteria1, priceCriteria2);
	    Criteria orCriteria2 = new Criteria().orOperator(priceCriteria4, priceCriteria5);

	    Criteria andCriteria = new Criteria().andOperator(priceCriteria3, orCriteria1,orCriteria2);
	    return Aggregation.match(andCriteria);
	} 
	
	private GroupOperation getGroupOperation() {
	    return Aggregation.group("isSubcribed")
	    		.count().as("numberOfUsers");
	}
	
	private ProjectionOperation getProjectOperation() {
	    return Aggregation.project("numberOfUsers");
	}

	private ProjectionOperation getProjectOperation1() {
	    return Aggregation.project("_id").andExpression("month(nextRenewalDate)").as("month")
	        .andExpression("year(nextRenewalDate)").as("year")
	        .andExpression("month(startSubcribedDate)").as("startSubcribedMonth")
	        .andExpression("year(startSubcribedDate)").as("startSubcribedYear")
	        .andExpression("isSubcribed").as("isSubcribed");
	}




}
