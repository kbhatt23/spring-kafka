package com.learning.kafkaproducer.basics.entity;

public class SimpleNumber {
	private int number;

	public SimpleNumber(int number) {
		super();
		this.number = number;
	}

	public SimpleNumber() {
		super();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "SimpleNumber [number=" + number + "]";
	}
	
	

}
