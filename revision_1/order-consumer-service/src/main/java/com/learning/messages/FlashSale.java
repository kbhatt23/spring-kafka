package com.learning.messages;

public class FlashSale {
	private String customerName;
	
	private String itemName;

	public FlashSale() {
		super();
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Override
	public String toString() {
		return "FlashSale [customerName=" + customerName + ", itemName=" + itemName + "]";
	}
	
	
}
