package com.learning.kafkaproducer.basics;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaproducer.basics.entity.FoodOrder;

//@Service
public class FoodOrderProducer implements CommandLineRunner {

	private static final String FOOD_ORDER_TOPIC = "food-order-topic";
	private static final String NUMBER_TOPIC = "number-topic";

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public void produce(FoodOrder foodOrder) throws JsonProcessingException {

		kafkaTemplate.send(FOOD_ORDER_TOPIC, objectMapper.writeValueAsString(foodOrder));
	}

	public void produce(int number) {
		kafkaTemplate.send(NUMBER_TOPIC, String.valueOf(number));
	}

	@Override
	public void run(String... args) throws Exception {
		produce(new FoodOrder("roti-sabzi", 101.99));
		produce(new FoodOrder("chole-chawal", 111.99));
		produce(new FoodOrder("dal-chawal", 99.99));

		IntStream.range(2, 6).forEach(this::produce);
	}
}
