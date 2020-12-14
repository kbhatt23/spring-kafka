package com.learning.kafkaconsumer.basics;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaconsumer.basics.entity.CarLocation;
import com.learning.kafkaconsumer.basics.entity.Employee;

//@Service
public class CarLocationConsumer {

	private static final String CAR_TOPIC="carlocation-topic";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	Logger logger = LoggerFactory.getLogger(CarLocationConsumer.class);
	
	//multiple groupd ids : same event will go to indepenent thread running listerners
	//so we need to add condition for filtering
	@KafkaListener(topics = CAR_TOPIC,groupId = "car-near" ,containerFactory = "carNearConsumerFactory")
	public void recieveNear(String carLocationStr) throws InterruptedException {
		
		try {
			CarLocation carLocation = objectMapper.readValue(carLocationStr, CarLocation.class);
			logger.info("reciveNearCarLocation: recieved car location :"+carLocation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	@KafkaListener(topics = CAR_TOPIC,groupId = "car-far",containerFactory = "carFarConsumerFactory")
	public void recieveFar(String carLocationStr) throws InterruptedException {
		try {
			CarLocation carLocation = objectMapper.readValue(carLocationStr, CarLocation.class);
			logger.info("reciveFarCarLocation: recieved car location :"+carLocation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
