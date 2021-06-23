package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dtos.OrderDTO;
import com.learning.messages.FlashSale;
import com.learning.messages.OrderFeedback;
import com.learning.services.OrderService;

@RestController
@RequestMapping("/flashsale")
public class FlashSAleController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<FlashSale> publishFeedBack(@RequestBody FlashSale flashSale){

		orderService.publishFlashSale(flashSale);
		return new ResponseEntity<FlashSale>(flashSale, HttpStatus.CREATED);
	}
}
