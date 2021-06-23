package com.learning.producers;

import java.util.Random;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//scheduled producer
//if this is enabled the application keeps on running
//@Service
public class FixedRateProducer{

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private static final String TOPIC_NAME = "fixedrate";
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	//after every 5 seconds it keeps calling this method
	@Scheduled(fixedRate = 5000)
	public void run() throws Exception {
		LOGGER.info("Sending Fixed rate messages to kafka");
		IntStream
			.rangeClosed(1, 10)
			.forEach(number ->
			//without key so randomly kafka will distribute to different partitions if topic has more than one partition
			kafkaTemplate.send(TOPIC_NAME, "jai shree ram says kannu-"+number)
			);
	}

	
}
