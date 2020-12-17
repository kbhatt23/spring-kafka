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
import com.learning.kafka.messages.PromotionMessage;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
	
	@Autowired
	private KafkaTemplate<String, PromotionMessage> kafkaTemplate;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PostMapping
	public ResponseEntity<PromotionMessage> createPromotion(@RequestBody PromotionMessage promotionMessage){
		
		kafkaTemplate.send(OrderServiceConstants.PROMOTION_DISCOUNT_TOPIC,OrderServiceConstants.PROMOTION_KEY ,promotionMessage)
		.addCallback(new ListenableFutureCallback<SendResult<String, PromotionMessage>>() {

			@Override
			public void onSuccess(SendResult<String, PromotionMessage> result) {

				logger.info("createPromotion: Promotion {} succesfully created",promotionMessage);
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.info("createPromotion: Promotion {} failed while creating with exception {}",promotionMessage,ex.getMessage());
			}
		});
		return new ResponseEntity<PromotionMessage>(promotionMessage, HttpStatus.CREATED);
	}
}
