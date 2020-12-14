package com.learning.kafkaconsumer.basics.entity;

public class CarLocation {

	private String carId;

	private long instanceTime;

	// distance in km from user's location
	private int distance;

	public CarLocation(String carId, int distance) {
		this.carId = carId;
		this.distance = distance;
		this.instanceTime = System.currentTimeMillis();
	}
	public CarLocation() {
	}
	
	public String getCarId() {
		return carId;
	}


	public void setCarId(String carId) {
		this.carId = carId;
	}


	public long getInstanceTime() {
		return instanceTime;
	}


	public void updateInstanceTime() {
		this.instanceTime = System.currentTimeMillis();
	}


	public int getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}


	@Override
	public String toString() {
		return "CarLocation [carId=" + carId + ", instanceTime=" + instanceTime + ", distance=" + distance + "]";
	}
	
	

}
