package com.learning.producers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
public class HelloWorldProducer implements CommandLineRunner{

	//we use spring kafka template to save time and reduce bugs
		//we could have used kafka without template but that will be time consuming and error prone 
		//that many bugs has to be resolved
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private static final String TOPIC_NAME = "helloworld";
	
	private Random random = new Random();
	
	@Override
	public void run(String... args) throws Exception {
		//without key so randomly kafka will distribute to different partitions if topic has more than one partition
		kafkaTemplate.send(TOPIC_NAME, "jai shree ram says kannu-"+random.nextInt());
	}

	
}
