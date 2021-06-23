package com.learning.consumers.entities;

public class InnerNonRelatableEntity {

	private int id;

	public InnerNonRelatableEntity(int id) {
		this.id = id;
	}

	public InnerNonRelatableEntity() {
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
		return "InnerNonRelatableEntity [id=" + id + "]";
	}
	
}
