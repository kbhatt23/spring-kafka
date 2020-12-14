package com.learning.kafkaproducer.basics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
//@Service
public class BasicTopicMessageProducer {

	private static final String BASIC_TOPIC_NAME="basic-hello";
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void produceBasicTopicMessage(String name) {
		kafkaTemplate.send(BASIC_TOPIC_NAME, "jai shree ram says "+name+ Math.random());
	}
}
