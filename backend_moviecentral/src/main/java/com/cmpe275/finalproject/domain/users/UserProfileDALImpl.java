package com.cmpe275.finalproject.domain.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.cmpe275.finalproject.domain.movie.Movie;

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

}
