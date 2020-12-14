package com.learning.kafkaconsumer.basics;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class FixedRateTopicConsumer {

	
	@KafkaListener(topics = "fixed-rate-topic")
	public void receiveFixedRateTopicMessage(String msg) {
		System.out.println("receiveFixedRateTopicMessage: Message recieved "+msg);
	}
}
