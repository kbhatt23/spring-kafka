package com.learning.producers.entities;

public class OrderEntity {

	private String orderID;
	
	private double amount;

	public OrderEntity(String orderID, double amount) {
		super();
		this.orderID = orderID;
		this.amount = amount;
	}

	public OrderEntity() {
		super();
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "OrderEntity [orderID=" + orderID + ", amount=" + amount + "]";
	}
	
	
}
