package com.learning.kafkaproducer.basics.entity;

public class Commodity {

	private String name;
	private double price;
	public Commodity(String name, double price) {
		this.name = name;
		this.price = price;
	}
	public Commodity() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
