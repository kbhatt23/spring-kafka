package com.learning.kafkaproducer.basics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaproducer.basics.entity.Image;

//@Service
public class ImageRetryProducer implements CommandLineRunner{

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void run(String... args) throws Exception {
		
		Image image1 = new Image("image-1", "jpeg", 10000l);
		Image image2 = new Image("image-2", "svc", 30000l);
		Image image3 = new Image("image-3", "smc", 5000l);
		
		sendToKafka(image1);
		sendToKafka(image2);
		sendToKafka(image3);
		
		
	}
	
	public void sendToKafka(Image image) throws JsonProcessingException {
		kafkaTemplate.send("retry-image-topic", image.getType(), 
				objectMapper.writeValueAsString(image)
				);
	}
	
	
	
	
}
