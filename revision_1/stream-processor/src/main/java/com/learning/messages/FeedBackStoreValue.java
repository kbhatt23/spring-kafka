package com.learning.messages;

public class FeedBackStoreValue {

	private long ratingCount;
	
	private long ratingSum;

	public FeedBackStoreValue(long ratingCount, long ratingSum) {
		super();
		this.ratingCount = ratingCount;
		this.ratingSum = ratingSum;
	}

	public FeedBackStoreValue() {
		super();
	}

	public long getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(long ratingCount) {
		this.ratingCount = ratingCount;
	}

	public long getRatingSum() {
		return ratingSum;
	}

	public void setRatingSum(long ratingSum) {
		this.ratingSum = ratingSum;
	}

	@Override
	public String toString() {
		return "FeedBackStoreValue [ratingCount=" + ratingCount + ", ratingSum=" + ratingSum + "]";
	}
	
}
