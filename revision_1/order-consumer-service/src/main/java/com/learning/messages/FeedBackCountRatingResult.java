package com.learning.messages;

public class FeedBackCountRatingResult {

	private long totalReviews;
	
	private double averageRating;

	public FeedBackCountRatingResult(long totalReviews, double averageRating) {
		super();
		this.totalReviews = totalReviews;
		this.averageRating = averageRating;
	}

	public FeedBackCountRatingResult() {
		super();
	}

	public long getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(long totalReviews) {
		this.totalReviews = totalReviews;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	@Override
	public String toString() {
		return "FeedBackCountRatingResult [totalReviews=" + totalReviews + ", averageRating=" + averageRating + "]";
	}
	
	
}
