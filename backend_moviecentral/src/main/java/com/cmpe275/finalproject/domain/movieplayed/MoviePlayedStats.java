package com.cmpe275.finalproject.domain.movieplayed;

import org.bson.types.ObjectId;

public class MoviePlayedStats {
	String title;
	
	int numberOfPlays;
	
	String _id;
	
	

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getNumberOfPlays() {
		return numberOfPlays;
	}

	public void setNumberOfPlays(int numberOfPlays) {
		this.numberOfPlays = numberOfPlays;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
