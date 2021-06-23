package com.learning.dtos;

public class EmployeeDTO {

	private String firstName;

	private String lastName;

	private int age;

	private String maritalStatus;

	public String getFirstName() {
		return firstName;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "EmployeeDTO [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + ", maritalStatus="
				+ maritalStatus + "]";
	}

}
