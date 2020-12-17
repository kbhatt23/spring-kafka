package com.learning.kafka.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.learning.kafka.dtos.OrderDTO;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue
	private Integer orderId;
	
	@Column(nullable = false, length = 50)
	private String orderNumber;
	
	@Column(nullable = false, length = 250)
	private String address;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime creationDate;
	
	@UpdateTimestamp
	private LocalDateTime lastModifiedDate;
	
	@Column(nullable = false, length = 50)
	private String cardNumber;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems;

	public Order(String orderNumber, String address, String cardNumber) {
		this.orderNumber = orderNumber;
		this.address = address;
		this.cardNumber=cardNumber;
	}
	public Order(OrderDTO orderDTO) {
		this(orderDTO.getOrderNumber(), orderDTO.getAddress(), orderDTO.getCardNumber());
	}
	public Order() {
		super();
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

	public void setOrderItems(List<OrderItem> orderItems) {
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

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	
	
	public void addItem(OrderItem orderItem) {
		if(orderItems == null)
			orderItems = new ArrayList<>();
		
		orderItems.add(orderItem);
		orderItem.setOrder(this);
		
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	
}
