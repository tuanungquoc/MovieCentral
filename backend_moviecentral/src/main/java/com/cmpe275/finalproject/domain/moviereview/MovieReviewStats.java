package com.cmpe275.finalproject.domain.moviereview;

public class MovieReviewStats {
	int reviewRate;
	
	int totalPerRateReview;

	public int getTotalPerRateReview() {
		return totalPerRateReview;
	}
	public void setTotalPerRateReview(int totalPerRateReview) {
		this.totalPerRateReview = totalPerRateReview;
	}
	public int getReviewRate() {
		return reviewRate;
	}
	public void setReviewRate(int reviewRate) {
		this.reviewRate = reviewRate;
	}
}
