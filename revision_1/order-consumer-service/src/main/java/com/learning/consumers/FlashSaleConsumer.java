package com.learning.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class FlashSaleConsumer {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@KafkaListener(topics = "flashsale-customers" , groupId = "big-data")
	public void flashSaleCustomersCount(ConsumerRecord<String,String> record) {
		logger.info("flashSaleCustomersCount: Recieved message "+record.value()+" with key "+record.key());
	}
	
	@KafkaListener(topics = "flashsale-votes" , groupId = "big-data")
	public void flashSaleVoteCount(ConsumerRecord<String,String> record) {
		logger.info("flashSaleVoteCount: Recieved message "+record.value()+" with key "+record.key());
	}
}
