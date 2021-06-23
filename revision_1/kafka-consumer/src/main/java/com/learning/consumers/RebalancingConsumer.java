package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class RebalancingConsumer {

	//not making it static just to allow freedom on logger name
	//in statis getClass wont work and hence extra work is reduce to copy in new classes
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	//literal has to be static and final -> actual constant or string literal
	private static final String TOPIC_NAME = "rebalancing";
	
	

	//the value to annotation should be literal or constant
	@KafkaListener(topics = { TOPIC_NAME } /* , groupId = "consumer-service-01" */
	,concurrency = "2"
			)
	public void listenMessage(String message) {
		LOGGER.info("Recieved Message "+message);
	}
}
