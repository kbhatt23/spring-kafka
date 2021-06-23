package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learning.messages.Discount;
import com.learning.messages.Promotion;
import com.learning.services.PromotionDiscountService;

@RestController
public class PromotionDiscountController {

	@Autowired
	private PromotionDiscountService promotionDiscountService;
	
	@PostMapping("/discount")
	public ResponseEntity<Discount> discount(@RequestBody Discount discount){
		promotionDiscountService.sendDiscount(discount);
		return new ResponseEntity<>(discount, HttpStatus.CREATED);
	}
	
	@PostMapping("/promotion")
	public ResponseEntity<Promotion> promotion(@RequestBody Promotion promotion){
		promotionDiscountService.sendPromotion(promotion);
		return new ResponseEntity<>(promotion, HttpStatus.CREATED);
	}
	
	@PostMapping("/generic-object")
	public ResponseEntity<Void> genericObject(){
		promotionDiscountService.sendGenericObject(new Object());
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
}
