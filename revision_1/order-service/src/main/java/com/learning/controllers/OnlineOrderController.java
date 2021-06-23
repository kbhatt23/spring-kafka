package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.messages.OnlineOrderMessage;
import com.learning.services.OrderService;

@RestController
@RequestMapping("/online-order")
public class OnlineOrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<OnlineOrderMessage> publishOnlineOrder(@RequestBody OnlineOrderMessage message){
		orderService.publishOnlineOrder(message);
		
		return new ResponseEntity<OnlineOrderMessage>(message, HttpStatus.CREATED);
	}
	
}
