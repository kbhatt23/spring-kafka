package com.learning.consumers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.consumers.entities.Image;
import com.learning.consumers.entities.SimpleNumber;

@Service
public class ImageRetryableConsumer {

	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static Map<Integer, Integer> imageTypeCount ;
	
	private static Map<Integer, Integer> imageRetryCount  ;
	
	static {
		imageTypeCount = new HashMap<>();
		imageTypeCount.put(3, 1);
		imageTypeCount.put(13, 4);
		
		imageRetryCount = new HashMap<>();
		imageRetryCount.put(3, 0);
		imageRetryCount.put(13, 0);
	}
	
	@KafkaListener(topics = {"imageretryabletopic"} ,
			/*errorHandler = "basicRetryableContainerFactory",*/  //no need as gloabal errr ahndler exist
			containerFactory = "basicRetryableContainerFactory"
			)
	public void processOrder(String imageStr) {
		Image image = null;
		try {
			image = objectMapper.readValue(imageStr, Image.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		logger.info("starting processing of image "+image);
		
		//TODO: will implement later
		//non recoverable , no retryable error
//		if(image.getSize()> 1000) {
//			throw new IllegalStateException("image size can not be greater than 1000");
//		}
		
		//for id 3  -> retry count is 2
		//for id 13  -> retry count is 4
		
		
		if( !imageTypeCount.keySet().contains(image.getId())) {
			//success
			//update consumer offset
			logger.info("succesfuly processed image "+image);
		}else {
			int currentCount = imageRetryCount.get(image.getId());
			int maxCount = imageTypeCount.get(image.getId());
			
			if(currentCount == maxCount) {
				logger.info("succesfuly processed image "+image);
			}else {
			//retry
				imageRetryCount.put(image.getId(), currentCount+1);
			throw new IllegalStateException("image cleaning web service is down");
			}
		}
		
		
	}

}
