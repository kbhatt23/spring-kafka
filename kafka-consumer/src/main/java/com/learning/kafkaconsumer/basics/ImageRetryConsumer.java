package com.learning.kafkaconsumer.basics;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaconsumer.basics.entity.Image;

//@Service
public class ImageRetryConsumer {

	@Autowired
	private ObjectMapper objectMapper;
	
	@KafkaListener(topics = "retry-image-topic", containerFactory = "retryFactory")
	public void recieveImage(String imageStr) throws JsonParseException, JsonMappingException, IOException {
		Image image = objectMapper.readValue(imageStr, Image.class);
		System.out.println("recieveImage:Starting Image processing for "+ image);
		if(image.getType().equals("svc")) {
			throw new RuntimeException("Image can not have svc format ");
		}
		System.out.println("recieveImage:Image processed succesfully "+ image);
		
	}
}
