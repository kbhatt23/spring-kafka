package com.learning.kafkaconsumer.basics;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaconsumer.basics.entity.Employee;

//@Service
public class JacksonAsStringConsumer {

	@Autowired
	private ObjectMapper objectMapper;
	
	Logger logger = LoggerFactory.getLogger(JacksonAsStringConsumer.class);
	
	@KafkaListener(topics = "json-as-string-partition",concurrency = "1")
	public void recieve(ConsumerRecord<String, String> messageRecord) throws InterruptedException {
		logger.info("receiveFixedRateTopicMessage: Message recieved {} with key {} and partition {}",messageRecord.value(),messageRecord.key(),messageRecord.partition());
		
		String value = messageRecord.value();
		try {
			Employee readValue = objectMapper.readValue(value, Employee.class);
			logger.info("receiveFixedRateTopicMessage: generated employee :"+readValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
