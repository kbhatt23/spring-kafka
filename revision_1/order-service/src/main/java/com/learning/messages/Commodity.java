package com.learning.messages;

public class Commodity {

	private String id;
	
	private String name;
	
	private boolean plastic;
	

	public Commodity() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPlastic() {
		return plastic;
	}

	public void setPlastic(boolean plastic) {
		this.plastic = plastic;
	}

	@Override
	public String toString() {
		return "Commodity [id=" + id + ", name=" + name + ", plastic=" + plastic + "]";
	}
	
	
}
