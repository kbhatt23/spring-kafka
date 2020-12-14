package com.learning.kafkaconsumer.basics.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.learning.kafkaconsumer.basics.CustomLocalDateDeSerializer;

public class Employee {

	private String name;
	private double salary;
	
	//date stored is complex and hence wont be ablw to deserialze it otherwise
	@JsonDeserialize(using = CustomLocalDateDeSerializer.class)
	private LocalDate dateOfBirth;
	
	public Employee(String name, double salary, LocalDate dateOfBirth) {
		super();
		this.name = name;
		this.salary = salary;
		this.dateOfBirth = dateOfBirth;
	}
	public Employee() {
		super();
	}
	@Override
	public String toString() {
		return "Employee [name=" + name + ", salary=" + salary + ", dateOfBirth=" + dateOfBirth + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	
	
}
