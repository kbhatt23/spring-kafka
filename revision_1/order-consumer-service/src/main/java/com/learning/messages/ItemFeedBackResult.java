package com.learning.messages;

import java.util.Map;

public class ItemFeedBackResult {

	private String itemName;
	
	//lets pass 1-10 like imdb
	private double averageRating;
	
	private Map<String, FeedBackCountRatingResult> allRatingMap;

	public ItemFeedBackResult() {
	}
	
	public ItemFeedBackResult(String itemName, double averageRating,Map<String, FeedBackCountRatingResult> allRatingMap) {
		this.itemName = itemName;
		this.averageRating = averageRating;
		this.allRatingMap=allRatingMap;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public Map<String, FeedBackCountRatingResult> getAllRatingMap() {
		return allRatingMap;
	}

	public void setAllRatingMap(Map<String, FeedBackCountRatingResult> allRatingMap) {
		this.allRatingMap = allRatingMap;
	}

	@Override
	public String toString() {
		return "ItemFeedBackResult [itemName=" + itemName + ", averageRating=" + averageRating + ", allRatingMap="
				+ allRatingMap + "]";
	}

}
