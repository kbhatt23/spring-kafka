package com.learning.kafka.productsservice.dtos;

import java.math.BigDecimal;

public record ProductDTO(String name, BigDecimal price, Integer quantity) {
	

}