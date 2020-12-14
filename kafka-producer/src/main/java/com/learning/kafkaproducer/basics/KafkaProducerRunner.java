package com.learning.kafkaproducer.basics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
class KafkaProducerRunner implements CommandLineRunner{

	@Autowired
	private BasicTopicMessageProducer producer;
	@Override
	public void run(String... args) throws Exception {
		producer.produceBasicTopicMessage("kanishk");
	}
	
}