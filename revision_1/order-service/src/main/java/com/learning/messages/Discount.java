package com.learning.messages;

public class Discount {

	private int discountID;
	
	private double discountAmount;

	public Discount(int discountID, double discountAmount) {
		this.discountID = discountID;
		this.discountAmount = discountAmount;
	}

	public Discount() {
	}

	public int getDiscountID() {
		return discountID;
	}

	public void setDiscountID(int discountID) {
		this.discountID = discountID;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	@Override
	public String toString() {
		return "Discount [discountID=" + discountID + ", discountAmount=" + discountAmount + "]";
	}
	
	
}
