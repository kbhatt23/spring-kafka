package com.learning.controllers;

import java.time.LocalDateTime;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.messages.OnlinePaymentMessage;

@RestController
@RequestMapping("/online-payment")
public class OnlinePaymentController {

	@Autowired
	private KafkaTemplate<String, OnlinePaymentMessage> kafkaTemplate;
	
	@PostMapping
	public ResponseEntity<OnlinePaymentMessage> publishOnlineOrder(@RequestBody OnlinePaymentMessage message){
		
		message.setCreationDate(LocalDateTime.now());
		ProducerRecord<String, OnlinePaymentMessage> producerRecord = new ProducerRecord<>("online-payment",  message.getOrderNumber(),  message);
		
		kafkaTemplate.send(producerRecord);
		
		return new ResponseEntity<OnlinePaymentMessage>(message, HttpStatus.CREATED);
	}
	
}
