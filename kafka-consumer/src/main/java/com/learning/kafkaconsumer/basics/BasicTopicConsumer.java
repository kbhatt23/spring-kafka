package com.learning.kafkaconsumer.basics;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class BasicTopicConsumer {

	
	@KafkaListener(topics = "basic-hello")
	public void receiveBasicTopicMessage(String msg) {
		System.out.println("receiveBasicTopicMessage: Message recieved "+msg);
	}
}
