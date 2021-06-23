package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dtos.OrderDTO;
import com.learning.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<OrderDTO> createORderAndPublish(@RequestBody OrderDTO orderDTO){

		orderService.crateAndPublish(orderDTO);
		return new ResponseEntity<OrderDTO>(orderDTO, HttpStatus.CREATED);
	}
}
