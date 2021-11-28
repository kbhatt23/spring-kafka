package com.learning.kafka_basics.entities;

import java.util.List;

public class Order {
	private String userId;

	private String orderId;
	
	private List<String> products;
	
	private double price;

	public Order(String userId, String orderId, List<String> products, double price) {
		super();
		this.userId = userId;
		this.orderId = orderId;
		this.products = products;
		this.price = price;
	}

	public Order() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Order [userId=" + userId + ", orderId=" + orderId + ", products=" + products + ", price=" + price + "]";
	}
	
	

}
