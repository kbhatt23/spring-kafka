package com.learning.messages;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class OnlinePaymentMessage {

	private String orderNumber;
	
	private double price;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime creationDate;
	
	private String cardNumber;

	public OnlinePaymentMessage(String orderNumber, double price, LocalDateTime creationDate, String cardNumber) {
		super();
		this.orderNumber = orderNumber;
		this.price = price;
		this.creationDate = creationDate;
		this.cardNumber = cardNumber;
	}

	public OnlinePaymentMessage() {
		super();
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Override
	public String toString() {
		return "OnlinePaymentMessage [orderNumber=" + orderNumber + ", price=" + price + ", creationDate="
				+ creationDate + ", cardNumber=" + cardNumber + "]";
	}

	
}
