package com.learning.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.producers.entities.Image;

//@Service
public class ImageRetryableProducer implements CommandLineRunner{

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void run(String... args) throws Exception {
		//good
		Image image1 = new Image(1, "jpg", 900);//good image
		
		//retyable images
		Image image2 = new Image(3, "svc", 600);//retry and recoverable image after 2 attemps
		Image image3 = new Image(13, "svc", 700);//retry and recoverable image after 4 attemps
	
		//non recoverable
		Image image4 = new Image(4, "png",1100);//non recoverable as size is more than what we can handle
		
		sendMessage(image1);
		sendMessage(image2);
		sendMessage(image3);
		sendMessage(image4);
	}
	
	private void sendMessage(Image image) {
		try {
			kafkaTemplate.send("imageretryabletopic", image.getType() , objectMapper.writeValueAsString(image));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	
}
