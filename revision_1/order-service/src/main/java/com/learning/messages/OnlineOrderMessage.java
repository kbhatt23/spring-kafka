package com.learning.messages;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.learning.util.LocalDateTimeDeserializer;
import com.learning.util.LocalDateTimeSerializer;

public class OnlineOrderMessage {

	private String orderId;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime creationDate;
	
	private String itemName;
	
	private String quantity;

	public OnlineOrderMessage(String orderId, LocalDateTime creationDate, String itemName, String quantity) {
		super();
		this.orderId = orderId;
		this.creationDate = creationDate;
		this.itemName = itemName;
		this.quantity = quantity;
	}

	public OnlineOrderMessage() {
		super();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
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
		return "OnlineOrderMessage [orderId=" + orderId + ", creationDate=" + creationDate + ", itemName=" + itemName
				+ ", quantity=" + quantity + "]";
	}
	
}
