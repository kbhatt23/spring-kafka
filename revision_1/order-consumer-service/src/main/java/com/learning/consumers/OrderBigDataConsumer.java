package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.messages.OrderMessage;
import com.learning.messages.OrderMessageBulkQuantity;

//@Service
public class OrderBigDataConsumer {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	//@KafkaListener(topics = "order-topic" , groupId = "big-data")
	public void consumeBigData(OrderMessage orderMessage) {
		
		logger.info("consumeBigData: Order message recieved "+orderMessage);
	}
	//@KafkaListener(topics = "order-transformed-topic" , groupId = "big-data")
	public void consumeTransformedBigData(OrderMessage orderMessage) {
		//automatically it can be deserialized
		//however package name of ordermessage.java file should be same as that in producer and processor api
		logger.info("consumeTransformedBigData: Order message recieved "+orderMessage);
	}
	
	//normal orders with no bulk
	@KafkaListener(topics = "order-transformed-topic" , groupId = "big-data")
	public void consumeTransformedFinalBigData(OrderMessage orderMessage) {
		//automatically it can be deserialized
		//however package name of ordermessage.java file should be same as that in producer and processor api
		logger.info("bigdata non bulk order: Order message recieved "+orderMessage);
	}
	
	
}
