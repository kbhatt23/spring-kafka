package com.learning.kafkaconsumer.basics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SimpleNumberConsumer {
	private static final String FOOD_ORDER_TOPIC = "number-topic";

	Logger logger = LoggerFactory.getLogger(SimpleNumberConsumer.class);

	// no error handler but still handles because of glbal error handler
	@KafkaListener(topics = FOOD_ORDER_TOPIC/* , errorHandler = "foodErrorHandler" */)
	public void recieve(String messageRecord) throws InterruptedException {
		int number = Integer.parseInt(messageRecord);
		logger.info("readNumber: recieved number :" + number);
		// not even
		if (number % 2 != 0) {
			throw new IllegalArgumentException("readNumber:number has to be divisible by 2 " + number);
		} else {
			logger.info("readNumber: succesfully processed number:" + number);
		}

	}
}
