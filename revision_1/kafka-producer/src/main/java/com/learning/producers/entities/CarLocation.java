package com.learning.producers.entities;

public class CarLocation {

	private String carID;
	
	private long timeStamp;
	
	//distance from base
	private long distance;

	public CarLocation(String carID, long timeStamp, long distance) {
		super();
		this.carID = carID;
		this.timeStamp = timeStamp;
		this.distance = distance;
	}

	public CarLocation() {
		super();
	}

	public String getCarID() {
		return carID;
	}

	public void setCarID(String carID) {
		this.carID = carID;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "CarLocation [carID=" + carID + ", timeStamp=" + timeStamp + ", distance=" + distance + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carID == null) ? 0 : carID.hashCode());
		return result;
	}

}
