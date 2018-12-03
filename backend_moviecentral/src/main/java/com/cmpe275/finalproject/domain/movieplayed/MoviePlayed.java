package com.cmpe275.finalproject.domain.movieplayed;

import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.cmpe275.finalproject.domain.movie.Movie;

public class MoviePlayed {
	
	@Id
	private ObjectId _id;
	
	private Movie movie;
	
	private ObjectId customerId;
	
	private ObjectId orderId;
	
	private LocalDateTime timePlayed;
	
	@NotNull
	@NotBlank
	private String movieAvailability;

	public ObjectId getOrderId() {
		return orderId;
	}

	public void setOrderId(ObjectId orderId) {
		this.orderId = orderId;
	}

	public String get_id() {
		return _id.toString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getCustomerId() {
		return customerId.toString();
	}

	public void setCustomerId(ObjectId cusomterId) {
		this.customerId = cusomterId;
	}

	public LocalDateTime getTimePlayed() {
		return timePlayed;
	}

	public void setTimePlayed(LocalDateTime timePlayed) {
		this.timePlayed = timePlayed;
	}

	public String getMovieAvailability() {
		return movieAvailability;
	}

	public void setMovieAvailability(String movieAvailability) {
		this.movieAvailability = movieAvailability;
	}
	
}

