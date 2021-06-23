package com.learning.messages;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class OnlineOrderPaymentMessage {

	private String orderId;

	private double price;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime paymentCreationDate;

	private String cardNumber;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime orderCreationDate;
	
	private String itemName;
	
	private String quantity;

	public OnlineOrderPaymentMessage(String orderId, double price, LocalDateTime paymentCreationDate, String cardNumber,
			LocalDateTime orderCreationDate, String itemName, String quantity) {
		super();
		this.orderId = orderId;
		this.price = price;
		this.paymentCreationDate = paymentCreationDate;
		this.cardNumber = cardNumber;
		this.orderCreationDate = orderCreationDate;
		this.itemName = itemName;
		this.quantity = quantity;
	}

	public OnlineOrderPaymentMessage() {
		super();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDateTime getPaymentCreationDate() {
		return paymentCreationDate;
	}

	public void setPaymentCreationDate(LocalDateTime paymentCreationDate) {
		this.paymentCreationDate = paymentCreationDate;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public LocalDateTime getOrderCreationDate() {
		return orderCreationDate;
	}

	public void setOrderCreationDate(LocalDateTime orderCreationDate) {
		this.orderCreationDate = orderCreationDate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "OnlineOrderPaymentMessage [orderId=" + orderId + ", price=" + price + ", paymentCreationDate="
				+ paymentCreationDate + ", cardNumber=" + cardNumber + ", orderCreationDate=" + orderCreationDate
				+ ", itemName=" + itemName + ", quantity=" + quantity + "]";
	}
	
	
}
