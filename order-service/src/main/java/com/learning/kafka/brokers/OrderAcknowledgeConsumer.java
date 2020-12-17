package com.learning.kafka.brokers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.kafka.OrderServiceConstants;
import com.learning.kafka.messages.OrderAcknowledgeMessage;

@Service
@KafkaListener(topics = OrderServiceConstants.ORDER_ACKNOWLEDGE_TOPIC)
public class OrderAcknowledgeConsumer {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@KafkaHandler
	public void consumeOrder(OrderAcknowledgeMessage message) {
		logger.info("Acknowledgeent Recieved for order {}",message);

	}
	
	

}
