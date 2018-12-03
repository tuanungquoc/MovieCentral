package com.cmpe275.finalproject.domain.movieplayed;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.repository.query.Param;

import com.cmpe275.finalproject.domain.movie.Movie;
import com.cmpe275.finalproject.domain.order.OrderStats;

public interface MoviePlayedDAL {
	List<MoviePlayedStats> getStatsMoviePlayedByDays(int days) ;

}
