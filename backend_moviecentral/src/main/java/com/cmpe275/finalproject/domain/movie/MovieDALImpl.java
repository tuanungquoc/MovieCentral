package com.cmpe275.finalproject.domain.movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public Page<Movie> searchMovieByKeyWord(List<String> keywords, Pageable pageable,Map<String,Object>filters) {
		//doing minimal search with title, actors, sypnosis and director
		String[] searchAttributes = {"title", "actors", "directors"};
		String[] filterAttributes = {"rating"};
		ArrayList querySearchFilter = new ArrayList<String>();
		//building search query

		List<String> compiledSearchQueryStr = new ArrayList();
		if(keywords != null) {
			for(int i  = 0 ; i < searchAttributes.length ; i++) {
				for(String keyword: keywords) {
					compiledSearchQueryStr.add( "{\""+  searchAttributes[i] + "\": {'$regex' : '" + keyword + "'" + "," +"'$options': 'i'" + "} }");
				}
			}
		}


		//building filter query
		
		List<String> compiledFilterQueryStr = new ArrayList();
		if(filters != null && filters.size() > 0) {
			
			for(int i = 0 ; i < filterAttributes.length ; i++) {
				compiledFilterQueryStr.add("{'" + filterAttributes[i] +"':{$eq:'" + filters.get(filterAttributes[i]) +"'}}");
			}
		}
		
		
		String searchQuery = "{$or:" + compiledSearchQueryStr.toString() + "}";

		String filterQuery =  "{$and:" + compiledFilterQueryStr.toString() + "}";

		String finalQuery;
		
		if(compiledSearchQueryStr.size() == 0 && compiledFilterQueryStr.size() == 0) {
			finalQuery = "{}";
		}else if(compiledSearchQueryStr.size() == 0) {
			finalQuery = filterQuery;
		}else if(compiledFilterQueryStr.size() == 0) {
			finalQuery = searchQuery;
		}else {
			querySearchFilter.add(searchQuery);
			querySearchFilter.add(filterQuery);
			finalQuery =  "{$and:" + querySearchFilter.toString() + "}";
		}
		
		
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