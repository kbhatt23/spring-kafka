package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.avro.Avro01;
import com.learning.avro.BackWardCompatibleMessage;

//@Service
public class BackWardCompatibleConsumer {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@KafkaListener(topics = {"avro-backward-topic"})
	public void consumeBackWardComaptible(BackWardCompatibleMessage obj) {
		logger.info("consumeBackWardComaptible: Consuming avro instance "+obj);
	}
}
