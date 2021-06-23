package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.messages.Commodity;
import com.learning.services.OrderService;

@RestController
@RequestMapping("/commodity")
public class CommodityController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<Commodity> publishCommodityOrder(@RequestBody Commodity commodity){

		return new ResponseEntity<Commodity>(commodity, HttpStatus.CREATED);
	}
}
