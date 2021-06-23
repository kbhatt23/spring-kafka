package com.learning.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.producers.entities.CarLocation;

//@Service
public class MessageFilterProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	// initial cars location
	// assume speed of all ars are same
	// car1 and car3 moves away and car2 moves towards base location
	private CarLocation car1 = new CarLocation("car-1", System.currentTimeMillis(), 80);
	private CarLocation car2 = new CarLocation("car-2", System.currentTimeMillis(), 120);
	private CarLocation car3 = new CarLocation("car-3", System.currentTimeMillis(), 80);

	// send message every second
	@Scheduled(fixedDelay = 2000 , initialDelay = 2000)
	public void sendCarsLocations() {

		try {
			kafkaTemplate.send("carlocations", objectMapper.writeValueAsString(car1));
			kafkaTemplate.send("carlocations", objectMapper.writeValueAsString(car2));
			kafkaTemplate.send("carlocations", objectMapper.writeValueAsString(car3));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		//update the next location now itself
		long newTimeStamp = System.currentTimeMillis();
		car1.setDistance(car1.getDistance()+10);//move aways with distance 10 every 2 seconds
		car1.setTimeStamp(newTimeStamp);
		
		car2.setDistance(car2.getDistance()-10);//move towards with distance 10 every 2 seconds
		car2.setTimeStamp(newTimeStamp);
		
		car3.setDistance(car3.getDistance()+20);//move aways with distance 20 every 2 seconds
		car3.setTimeStamp(newTimeStamp);
	}

}
