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
	
	public ObjectId cusomterId;
	
	public ObjectId movieId;
	
	public int reviewRate;
	
	public String reviewText;
	
	public Date created;
	
	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public ObjectId getCusomterId() {
		return cusomterId;
	}

	public void setCusomterId(ObjectId cusomterId) {
		this.cusomterId = cusomterId;
	}

	public ObjectId getMovieId() {
		return movieId;
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
