package com.learning.messages.nonrelatable;

//package for the producer,cosnumer and stream processors can be different as we are using objectmaper
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
