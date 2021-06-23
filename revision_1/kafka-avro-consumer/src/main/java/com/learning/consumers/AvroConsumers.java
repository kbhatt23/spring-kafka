package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.avro.Avro01;

//@Service
public class AvroConsumers {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@KafkaListener(topics = {"avro-01-topic"})
	public void consumeAvro01(Avro01 obj) {
		logger.info("consumeAvro01: Consuming avro instance "+obj);
	}
}
