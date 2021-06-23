package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.messages.OrderMessage;
//@Service
public class OrderNotificationConsumer {

	Logger logger = LoggerFactory.getLogger(getClass());

	// @KafkaListener(topics = "order-topic" , groupId = "order-notification")
	public void consumeOrderNotification(OrderMessage orderMessage) {

		logger.info("consumeOrderNotification: Order message recieved " + orderMessage);
	}

	@KafkaListener(topics = "order-transformed-topic", groupId = "order-notification")
	public void consumeTransformedOrderNotification(OrderMessage orderMessage) {

		logger.info("notification non bulk order: Order message recieved " + orderMessage);
	}
}
