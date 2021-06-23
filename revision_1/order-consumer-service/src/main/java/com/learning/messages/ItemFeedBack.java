package com.learning.messages;

public class ItemFeedBack {

	private String itemName;
	
	//lets pass 1-10 like imdb
	private int rating;

	public ItemFeedBack() {
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "ItemFeedBack [itemName=" + itemName + ", rating=" + rating + "]";
	}

}
