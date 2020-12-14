package com.learning.kafkaproducer.basics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

//@Component
class KafkaMultiPArtitionProducerRunner implements CommandLineRunner {

	private static final String MULTI_PARITION_TOPIC = "multi-partition-topic";

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public void run(String... args) throws Exception {
		// the vlue of key will be used to club togher same valued messages to same
		// partition
		// automatically it will be hashed to schedule to approoerate partition
		//paritioning 
		for (int i = 1; i <= 40; i++) {
			String key = "";
			if (i < 11 && i > 0) {
				key= "key-1";
			} else if (i < 21 && i > 10) {
				key= "key-2";
			} else if (i < 31 && i > 20) {
				key= "key-3";
			} else if (i < 41 && i >30) {
				key= "key-4";
			}
			System.out.println("Sending message with key "+key);
			kafkaTemplate.send(MULTI_PARITION_TOPIC, key, "jai shree krishna : "+i);
		}
	}

}