package com.learning.messages;

public class FakeMessage {

	private String name;

	public FakeMessage(String name) {
		super();
		this.name = name;
	}

	public FakeMessage() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FakeMessage [name=" + name + "]";
	}
	
	
}
