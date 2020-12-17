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
import com.learning.kafkaconsumer.basics.entity.FoodOrder;

//@Service
public class FoodORderConsumer {
	private static final String FOOD_ORDER_TOPIC = "food-order-topic";
	@Autowired
	private ObjectMapper objectMapper;

	Logger logger = LoggerFactory.getLogger(FoodORderConsumer.class);

	//custom error handler
	//global error handler defined in seperate config
	@KafkaListener(topics = FOOD_ORDER_TOPIC, errorHandler = "foodErrorHandler" )
	public void recieve(String messageRecord) throws InterruptedException {
		FoodOrder readValue = null;
		try {
			readValue = objectMapper.readValue(messageRecord, FoodOrder.class);
			logger.info("readValueFoodOrder: recieved food order :" + readValue);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (readValue != null) {
			if(readValue.getAmount() > 110) {
				throw new IllegalArgumentException("Amount can not be too high : order recieved "+readValue);
			}else {
				logger.info("readValueFoodOrder: succesfully processed food order :" + readValue);
			}
		}

	}
}
