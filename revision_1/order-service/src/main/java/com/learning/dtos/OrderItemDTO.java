package com.learning.dtos;

import com.learning.entities.OrderItem;

public class OrderItemDTO {
	private Integer itemId;

	private String itemName;

	private double price;

	private int quantity;

	public OrderItemDTO(OrderItem orderItem) {
		this.itemId = orderItem.getItemId();
		this.itemName = orderItem.getItemName();
		this.price = orderItem.getPrice();
		this.quantity = orderItem.getQuantity();
	}

	public OrderItemDTO() {
		super();
	}

	@Override
	public String toString() {
		return "OrderItemDTO [itemId=" + itemId + ", itemName=" + itemName + ", price=" + price + ", quantity=" + quantity
				+ "]";
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

}
