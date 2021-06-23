package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.messages.InventoryItem;
import com.learning.services.OrderService;

@RestController
@RequestMapping("/inventory-update") 

public class InventoryItemController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<InventoryItem> publishInventory(@RequestBody InventoryItem item){
		orderService.publishInventoryUpdate(item);
		
		return new ResponseEntity<InventoryItem>(item,HttpStatus.CREATED);
	}
}
