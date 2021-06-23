package com.learning.consumers.entities;

public class Image {

	//for 3 and 13 id image clearing service will break, but it can recover after few attempts 
	private int id;

	private String type;

	// if size is more than 1000 then we can not handle and hence should not retry
	// directly ignore and let error hadnler swallow it
	private long size;

	public Image(int id, String type, long size) {
		this.id = id;
		this.type = type;
		this.size = size;
	}

	public Image() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return "Image [id=" + id + ", type=" + type + ", size=" + size + "]";
	}

}
