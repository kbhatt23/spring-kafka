package com.learning.kafka.productsservice.controllers;

import com.learning.kafka.productsservice.dtos.ProductDTO;
import com.learning.kafka.productsservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductService productService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO){
        try{
            String productID = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(productID);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return ResponseEntity.internalServerError().body(null);
        }

    }
}
