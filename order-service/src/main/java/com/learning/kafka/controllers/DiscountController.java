package com.learning.kafka.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.kafka.OrderServiceConstants;
import com.learning.kafka.messages.DiscountMessage;

@RestController
@RequestMapping("/discount")
public class DiscountController {
	
	@Autowired
	private KafkaTemplate<String, DiscountMessage> kafkaTemplate;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PostMapping
	public ResponseEntity<DiscountMessage> createDiscount(@RequestBody DiscountMessage discountMessage){
		
		kafkaTemplate.send(OrderServiceConstants.PROMOTION_DISCOUNT_TOPIC,OrderServiceConstants.DISCOUNT_KEY ,discountMessage)
		.addCallback(new ListenableFutureCallback<SendResult<String, DiscountMessage>>() {

			@Override
			public void onSuccess(SendResult<String, DiscountMessage> result) {

				logger.info("createDiscount: Promotion {} succesfully created",discountMessage);
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.info("createDiscount: Promotion {} failed while creating with exception {}",discountMessage,ex.getMessage());
			}
		});
		return new ResponseEntity<DiscountMessage>(discountMessage, HttpStatus.CREATED);
	}
}
