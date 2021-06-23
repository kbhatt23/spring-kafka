package com.learning.producers.entities;

public class SimpleNumber {

	private int id;

	public SimpleNumber(int id) {
		super();
		this.id = id;
	}

	public SimpleNumber() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SimpleNumber [id=" + id + "]";
	}
	
	
}
