package com.learning.kafka_basics.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka_basics.config.KafkaConfig;
import com.learning.kafka_basics.entities.User;

public class UserDataProducer {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final String OUTPUT_TOPIC = "user-data";
	private static KafkaProducer<String, String> producer ;

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVER);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.APPLICATION_ID);
		

		producer = new KafkaProducer<>(properties);
		
		User user = new User("kbhatt23", "Kanishk Bhatt", "kbhatt23@gmail.com");
		String userStr;
		try {
			userStr = OBJECT_MAPPER.writeValueAsString(user);
			//key as userId and value as user object
			ProducerRecord<String, String> producerRecord = new ProducerRecord<>(OUTPUT_TOPIC, user.getUserId(), userStr);
			producer.send(producerRecord);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		//cleanup
		producer.flush();
		producer.close();
	}

}
