package com.cmpe275.finalproject.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.repository.support.PageableExecutionUtils;

public class MovieDALImpl implements MovieDAL{
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Movie> getAllMovies() {
		// TODO Auto-generated method stub
		return mongoTemplate.findAll(Movie.class);

	}

	@Override
	public Page<Movie> searchMovieByKeyWord(List<String> keywords, Pageable pageable) {
		//doing minimal search with title, actors, sypnosis and director
		String[] tagNames = {"title", "actors", "directors"};
		
		
		List<String> compiledQueryStr = new ArrayList();
		for(int i  = 0 ; i < tagNames.length ; i++) {
			for(String keyword: keywords) {
				compiledQueryStr.add( "{\""+  tagNames[i] + "\": {'$regex' : '" + keyword + "'" + "," +"'$options': 'i'" + "} }");
			}
		}
		
		String finalQuery = "{$or:" + compiledQueryStr.toString() + "}";
	
		BasicQuery query = 
			new BasicQuery(finalQuery);
		query.with(pageable);
		Page<Movie> moviePage = PageableExecutionUtils.getPage(
				mongoTemplate.find(query, Movie.class),
		        pageable,
		        () -> mongoTemplate.count(query, Movie.class));
		
		return moviePage;
	}
	
}
