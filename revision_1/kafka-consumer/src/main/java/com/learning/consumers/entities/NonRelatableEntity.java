package com.learning.consumers.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

//this class will be in different package in producer and consumer applications
public class NonRelatableEntity {

	//in class property name is 'name' but in json for kafka it will be 'king_name'
	@JsonProperty(value = "king_name")
	private String name;
	
	private String description;
	
	private InnerNonRelatableEntity innerNonRelatableEntity;

	public NonRelatableEntity(String name, String description, InnerNonRelatableEntity innerNonRelatableEntity) {
		this.name = name;
		this.description = description;
		this.innerNonRelatableEntity=innerNonRelatableEntity;
	}
	//for jackson
	public NonRelatableEntity() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public InnerNonRelatableEntity getInnerNonRelatableEntity() {
		return innerNonRelatableEntity;
	}
	public void setInnerNonRelatableEntity(InnerNonRelatableEntity innerNonRelatableEntity) {
		this.innerNonRelatableEntity = innerNonRelatableEntity;
	}
	@Override
	public String toString() {
		return "NonRelatableEntity [name=" + name + ", description=" + description + ", innerNonRelatableEntity="
				+ innerNonRelatableEntity + "]";
	}

	
}
