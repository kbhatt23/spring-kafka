package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.consumers.entities.CarLocation;

//@Service
public class MessageFilterConsumerManual {

	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@KafkaListener(topics = {"carlocations"})
	public void allCarLocations(String carLoactionStr) {
		
		try {
			CarLocation readValue = objectMapper.readValue(carLoactionStr, CarLocation.class);
			if(readValue.getDistance() < 0 || readValue.getDistance() > 500) {
				//outside of min and max range
				return;
			}
			LOGGER.info("allCarLocations: recieved carlocation "+readValue);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@KafkaListener(topics = {"carlocations"} , groupId = "consumer-service-02")
	public void closeCarLocations(String carLoactionStr) {
		
		try {
			CarLocation readValue = objectMapper.readValue(carLoactionStr, CarLocation.class);
			//filtering logic is at same location as consuming logic
			//breaking single responsigbility principle and making worker thread work on wrong data/unnecesarry work
			if(readValue.getDistance() > 100 || readValue.getDistance() < 0) {
				return;
			}
			LOGGER.info("closeCarLocations: recieved carlocation "+readValue);
			//business logic
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
