package com.learning.kafkaconsumer.basics.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.learning.kafkaconsumer.basics.CustomLocalDateDeSerializer;

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
	@Override
	public String toString() {
		return "Commodity [name=" + name + ", price=" + price + "]";
	}
	
	
	
}
