package com.learning.producers;

import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//scheduled producer
//if this is enabled the application keeps on running
//@Service
public class ConcurrentProducer implements CommandLineRunner{

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private static final String TOPIC_NAME = "multipartitionedtopic";
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	

	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("Sending concurretly partitioned messages");
		
		//from 1 to 90
		IntStream.rangeClosed(1, 90)
				.mapToObj(i -> {
					String key = "N/A";
					if( i <= 30) {
						key = "1";
					}else if( i <= 60) {
						key = "2";
					}else if( i <= 90){
						key = "3";
					}
					return key+":"+i;
					})//convert to key string
				.forEach(keyStr ->{
					String key = keyStr.split(":")[0];
					String value = keyStr.split(":")[1];
					LOGGER.info("complete partition producer data "+key+" divided by "+value);
					kafkaTemplate.send(TOPIC_NAME, key, "jai shree ram says "+value);
				});
	}

	
}
