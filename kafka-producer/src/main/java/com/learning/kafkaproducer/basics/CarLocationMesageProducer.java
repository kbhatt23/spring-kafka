package com.learning.kafkaproducer.basics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaproducer.basics.entity.CarLocation;

//@Service
public class CarLocationMesageProducer {

	private static final String CAR_TOPIC="carlocation-topic";
	private CarLocation car1;
	private CarLocation car2;
	private CarLocation car3;
	//no arg constructor gets called by spring at singleton instnace creation
	public CarLocationMesageProducer() {
		car1 = new CarLocation("Car-1", 100);
		car2 = new CarLocation("Car-2", 150);
		car3 = new CarLocation("Car-3", 70);
	}
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	//every 5 second we update the car's location , car can move towards or away from user location
	@Scheduled(fixedRate = 5000)
	public void produce() throws JsonProcessingException {
		car1.setDistance(car1.getDistance()+10);
		car1.updateInstanceTime();
		
		car2.setDistance(car2.getDistance()-10);
		car2.updateInstanceTime();
		
		car3.setDistance(car3.getDistance()+10);
		car3.updateInstanceTime();
		
		kafkaTemplate.send(CAR_TOPIC,objectMapper.writeValueAsString(car1));
		kafkaTemplate.send(CAR_TOPIC,objectMapper.writeValueAsString(car2));
		kafkaTemplate.send(CAR_TOPIC,objectMapper.writeValueAsString(car3));
	}
}
