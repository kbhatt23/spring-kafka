package com.learning.kafka.events;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductCreatedEvent {
    private String productID;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    //to be used by consumer to deserialize
    public ProductCreatedEvent() {
    }

    public ProductCreatedEvent(String productID, String name, BigDecimal price, Integer quantity) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCreatedEvent that = (ProductCreatedEvent) o;
        return Objects.equals(productID, that.productID) &&
                Objects.equals(name, that.name) &&
                Objects.equals(price, that.price) &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, name, price, quantity);
    }
}
