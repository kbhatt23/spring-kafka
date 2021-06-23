package com.learning.model;

import java.util.Arrays;

public class Order {

	private String id;
	
	private double price;
	
	private String[] items;

	public Order(String id, double price, String[] items) {
		this.id = id;
		this.price = price;
		this.items = items;
	}

	public Order() {
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", price=" + price + ", items=" + Arrays.toString(items) + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String[] getItems() {
		return items;
	}

	public void setItems(String[] items) {
		this.items = items;
	}
	
	
}
