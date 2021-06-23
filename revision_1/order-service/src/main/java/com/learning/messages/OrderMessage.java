package com.learning.messages;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.learning.entities.Order;
import com.learning.entities.OrderItem;

//represent flattened order have everything for an order item plus order
public class OrderMessage {
	//order starts
		private Integer orderId;
		
		private String orderNumber;
		
		private String address;
		
		@JsonSerialize(using = LocalDateTimeSerializer.class)
		@JsonDeserialize(using = LocalDateTimeDeserializer.class)
		private LocalDateTime creationDate;
		
		@JsonSerialize(using = LocalDateTimeSerializer.class)
		@JsonDeserialize(using = LocalDateTimeDeserializer.class)
		private LocalDateTime lastModifiedDate;
		
		private String cardNumber;
		//order ends
		
		//orderITems starts
		private Integer itemId;
		
		private String itemName;
		
		private double price;
		
		private int quantity;
		//orderITems ends

		public OrderMessage() {
		}
		
		public OrderMessage(Order order,OrderItem orderItem) {
			this.orderId=order.getOrderId();
			this.orderNumber = order.getOrderNumber();
			this.address = order.getAddress();
			this.creationDate=order.getCreationDate();
			this.lastModifiedDate = order.getLastModifiedDate();
			this.cardNumber=order.getCardNumber();
			this.itemId=orderItem.getItemId();
			this.itemName = orderItem.getItemName();
			this.price=orderItem.getPrice();
			this.quantity=orderItem.getQuantity();
			
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

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public LocalDateTime getCreationDate() {
			return creationDate;
		}

		public void setCreationDate(LocalDateTime creationDate) {
			this.creationDate = creationDate;
		}

		public LocalDateTime getLastModifiedDate() {
			return lastModifiedDate;
		}

		public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		public String getCardNumber() {
			return cardNumber;
		}

		public void setCardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
		}

		public Integer getItemId() {
			return itemId;
		}

		public void setItemId(Integer itemId) {
			this.itemId = itemId;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		@Override
		public String toString() {
			return "OrderMessage [orderId=" + orderId + ", orderNumber=" + orderNumber + ", address=" + address
					+ ", creationDate=" + creationDate + ", lastModifiedDate=" + lastModifiedDate + ", cardNumber="
					+ cardNumber + ", itemId=" + itemId + ", itemName=" + itemName + ", price=" + price + ", quantity="
					+ quantity + "]";
		}
}
