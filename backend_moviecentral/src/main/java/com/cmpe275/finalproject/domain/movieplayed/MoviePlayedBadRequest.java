package com.cmpe275.finalproject.domain.movieplayed;

public class MoviePlayedBadRequest {
	String movieId;
	
	double total;
	
	boolean isSubribed;
	
	String typeOfMovie;

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public boolean isSubribed() {
		return isSubribed;
	}

	public void setSubribed(boolean isSubribed) {
		this.isSubribed = isSubribed;
	}

	public String getTypeOfMovie() {
		return typeOfMovie;
	}

	public void setTypeOfMovie(String typeOfMovie) {
		this.typeOfMovie = typeOfMovie;
	}
	
	
}
