package com.learning.messages;

public class Promotion {

	private int promotionID;
	
	private double discountPercentage;

	public Promotion(int promotionID, double discountPercentage) {
		this.promotionID = promotionID;
		this.discountPercentage = discountPercentage;
	}

	public Promotion() {
	}

	public int getPromotionID() {
		return promotionID;
	}

	public void setPromotionID(int promotionID) {
		this.promotionID = promotionID;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	@Override
	public String toString() {
		return "Promotion [promotionID=" + promotionID + ", discountPercentage=" + discountPercentage + "]";
	}
	
	
}
