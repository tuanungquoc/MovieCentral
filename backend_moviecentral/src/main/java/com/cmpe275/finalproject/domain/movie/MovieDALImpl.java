package com.cmpe275.finalproject.domain.movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
		String[] filterAttributes = {"rating", "year","genre","actors","directors","numberOfStars"};
		ArrayList querySearchFilter = new ArrayList<String>();
		//building search query

		List<String> compiledSearchQueryStr = new ArrayList();
		if(keywords != null) {
			for(int i  = 0 ; i < searchAttributes.length ; i++) {
				for(String keyword: keywords) {
					if(!keyword.toLowerCase().equals("the") && 
							!keyword.toLowerCase().equals("a") && 
							!keyword.toLowerCase().equals("an"))
					compiledSearchQueryStr.add( "{\""+  searchAttributes[i] + "\": {'$regex' : '" + keyword + "'" + "," +"'$options': 'i'" + "} }");
				}
			}
		}


		//building filter query

		List<String> compiledFilterQueryStr = new ArrayList();
		if(filters != null && filters.size() > 0) {

			for(int i = 0 ; i < filterAttributes.length ; i++) {
				if(filters.get(filterAttributes[i])!=null) {
					if(filterAttributes[i].equals("year") || filterAttributes[i].equals("numberOfStars")) {
						compiledFilterQueryStr.add("{'" + filterAttributes[i] +"':{$eq:" + filters.get(filterAttributes[i]) +"}}");
					}else if(filterAttributes[i].equals("actors") || filterAttributes[i].equals("directors")) {
						compiledFilterQueryStr.add("{\""+  filterAttributes[i] + "\": {'$regex' : '" + filters.get(filterAttributes[i]) + "'" + "," +"'$options': 'i'" + "} }");
					}else{
						compiledFilterQueryStr.add("{'" + filterAttributes[i] +"':{$eq:'" + filters.get(filterAttributes[i]) +"'}}");
					}
				}
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

	@Override
	public Page<Movie> getAllMoviesByRating(Pageable pageable) {
		BasicQuery query = 
				new BasicQuery("{}");
		query.with(pageable);
		query.with(new Sort(Sort.Direction.DESC, "numberOfStars"));
		
		Page<Movie> moviePage = PageableExecutionUtils.getPage(
				mongoTemplate.find(query, Movie.class),pageable,() -> mongoTemplate.count(query, Movie.class));

		return moviePage;
	}

}
