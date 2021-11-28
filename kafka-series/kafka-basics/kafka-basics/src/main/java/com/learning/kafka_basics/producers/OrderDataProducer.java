package com.learning.kafka_basics.producers;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka_basics.config.KafkaConfig;
import com.learning.kafka_basics.entities.Order;

public class OrderDataProducer {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final String OUTPUT_TOPIC = "order-data";
	private static KafkaProducer<String, String> producer ;

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVER);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.APPLICATION_ID);
		

		producer = new KafkaProducer<>(properties);
		
		Order order = new Order("kbhatt23", UUID.randomUUID().toString(), Arrays.asList("apple","mango","phone","laptop"), 140000);
		String orderStr;
		try {
			orderStr = OBJECT_MAPPER.writeValueAsString(order);
			//key as userId and value as user object
			ProducerRecord<String, String> producerRecord = new ProducerRecord<>(OUTPUT_TOPIC, order.getUserId(), orderStr);
			producer.send(producerRecord);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		//cleanup
		producer.flush();
		producer.close();
	}

}
