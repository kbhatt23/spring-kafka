package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.messages.OrderMessage;
import com.learning.messages.OrderMessageBulkQuantity;

//@Service
public class BulkOrderConsumer {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	//@KafkaListener(topics = "order-topic" , groupId = "big-data")
	public void consumeBigData(OrderMessage orderMessage) {
		
		logger.info("consumeBigData: Order message recieved "+orderMessage);
	}
	//only bulk order comes
	//also it has tottal price and not unit price
	//wont come for non bulk orders at all
	@KafkaListener(topics = "order-bulk-topic" , groupId = "big-data")
	public void consumeBulkOrder(OrderMessageBulkQuantity orderMessage) {
		//automatically it can be deserialized
		//however package name of ordermessage.java file should be same as that in producer and processor api
		logger.info("consumeBulkOrder: Order message recieved "+orderMessage);
	}
	
	
}
