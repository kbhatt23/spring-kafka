package com.learning.kafkaproducer.basics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//@Service
public class RebalncingTopicMessageProducer {

private static final String FIXED_RATE_TOPIC="fixed-rate-topic";
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	private int index;
	
	@Scheduled(fixedRate = 1000)
	public void produce() {
		String name = "kanishk";
		kafkaTemplate.send(FIXED_RATE_TOPIC, "jai shree ram says "+name+ " : "+index);
		index++;
	}
}
