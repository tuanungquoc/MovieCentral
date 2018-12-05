package com.cmpe275.finalproject.domain.moviereview;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.cmpe275.finalproject.validation.ValidEmail;

public class MovieReview {
	@Id
	public ObjectId _id;
	
	public ObjectId customerId;
	
	public String profileName;
	
	

	public ObjectId movieId;
	
	public int reviewRate;
	
	public String reviewText;
	
	public Date created;
	
	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public String get_id() {
		return _id.toString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getCustomerId() {
		return customerId.toString();
	}

	public void setCustomerId(ObjectId customerId) {
		this.customerId = customerId;
	}

	public String getMovieId() {
		return movieId.toString();
	}

	public void setMovieId(ObjectId movieId) {
		this.movieId = movieId;
	}

	public int getReviewRate() {
		return reviewRate;
	}

	public void setReviewRate(int reviewRate) {
		this.reviewRate = reviewRate;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
