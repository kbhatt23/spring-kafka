package com.learning.kafka.messages;

public class OrderAcknowledgeMessage {

	private Integer orderId;
	
	private String orderNumber;
	
	private Integer itemId;

	public OrderAcknowledgeMessage(Integer orderId, String orderNumber, Integer itemId) {
		super();
		this.orderId = orderId;
		this.orderNumber = orderNumber;
		this.itemId = itemId;
	}

	public OrderAcknowledgeMessage() {
		super();
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "OrderAcknowledgeMessage [orderId=" + orderId + ", orderNumber=" + orderNumber + ", itemId=" + itemId
				+ "]";
	}
	
	
}
