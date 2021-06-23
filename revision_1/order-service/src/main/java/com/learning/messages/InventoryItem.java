package com.learning.messages;

public class InventoryItem {

	private String itemName;
	
	private long quantity;
	
	private String location;
	
	//BUY,SELL
	private ItemType itemType;

	public InventoryItem() {
		super();
	}

	public InventoryItem(String itemName, long quantity, String location, ItemType type) {
		super();
		this.itemName = itemName;
		this.quantity = quantity;
		this.location = location;
		this.itemType = type;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	@Override
	public String toString() {
		return "InventoryItem [itemName=" + itemName + ", quantity=" + quantity + ", location=" + location
				+ ", itemType=" + itemType + "]";
	}


	
}
