package com.learning.entities;

import java.util.List;

public class UserOrderData {

	private String userId;

	private String orderId;
	
	private List<String> products;
	
	private double price;
	
	private String name;
	
	private String email;

	public UserOrderData(String userId, String orderId, List<String> products, double price, String name,
			String email) {
		super();
		this.userId = userId;
		this.orderId = orderId;
		this.products = products;
		this.price = price;
		this.name = name;
		this.email = email;
	}

	public UserOrderData() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserOrderData [userId=" + userId + ", orderId=" + orderId + ", products=" + products + ", price="
				+ price + ", name=" + name + ", email=" + email + "]";
	}
	
	
}
