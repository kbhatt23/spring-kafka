package com.learning.kafkaproducer.basics.entity;

public class Invoice {

	private String id;
	private double price;
	public Invoice(String id, double price) {
		super();
		this.id = id;
		this.price = price;
	}
	public Invoice() {
		super();
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
	@Override
	public String toString() {
		return "Invoice [id=" + id + ", price=" + price + "]";
	}
	
	
}
