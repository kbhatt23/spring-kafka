package com.learning.kafka.productsservice.services;

import com.learning.kafka.productsservice.dtos.ProductDTO;

public interface ProductService {
	public String createProduct(ProductDTO productDTO) throws Exception ;

}
