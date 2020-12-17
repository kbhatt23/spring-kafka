package com.learning.kafkaproducer.basics.entity;

public class Image {

	private String name;
	//jpeg,svc etc
	private String type;
	
	private long size;

	public Image(String name, String type, long size) {
		super();
		this.name = name;
		this.type = type;
		this.size = size;
	}

	public Image() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Image [name=" + name + ", type=" + type + ", size=" + size + "]";
	}
	
	
}
