package com.learning.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.learning.entities.Order;

public class OrderDTO {
private Integer orderId;
	
	private String orderNumber;
	
	private String address;
	
	private LocalDateTime creationDate;
	
	private LocalDateTime lastModifiedDate;
	
	private String cardNumber;
	
	private List<OrderItemDTO> orderItems;


	public OrderDTO(Order order) {
		this.orderId=order.getOrderId();
		this.cardNumber = order.getCardNumber();
		this.orderNumber = order.getOrderNumber();
		this.creationDate=order.getCreationDate();
		this.lastModifiedDate=order.getLastModifiedDate();
		this.address=order.getAddress();
		this.orderItems = order.getOrderItems().stream().map(OrderItemDTO::new).collect(Collectors.toList());
	}

	public OrderDTO() {
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public void setOrderItems(List<OrderItemDTO> orderItems) {
		this.orderItems = orderItems;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public List<OrderItemDTO> getOrderItems() {
		return orderItems;
	}
	
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Override
	public String toString() {
		return "OrderDTO [orderId=" + orderId + ", orderNumber=" + orderNumber + ", address=" + address
				+ ", creationDate=" + creationDate + ", lastModifiedDate=" + lastModifiedDate + ", cardNumber="
				+ cardNumber + ", orderItems=" + orderItems + "]";
	}
}
