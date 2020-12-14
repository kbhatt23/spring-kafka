package com.learning.kafkaproducer.basics.entity;

public class FoodOrder {

	private String name;
	private double amount;
	public FoodOrder(String name, double amount) {
		super();
		this.name = name;
		this.amount = amount;
	}
	public FoodOrder() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "FoodOrder [name=" + name + ", amount=" + amount + "]";
	}
	
	
}
