package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.messages.ItemFeedBack;
import com.learning.services.OrderService;

@RestController
@RequestMapping("/feedback")
public class FeedBackController {
	
	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<ItemFeedBack> publishFeedBack(@RequestBody ItemFeedBack itemFeedBack){
		
		orderService.publishFeedBack(itemFeedBack);
		
		return new ResponseEntity<ItemFeedBack>(itemFeedBack, HttpStatus.CREATED);
	}
}
