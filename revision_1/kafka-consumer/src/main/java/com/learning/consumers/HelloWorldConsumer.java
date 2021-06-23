package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class HelloWorldConsumer {

	//not making it static just to allow freedom on logger name
	//in statis getClass wont work and hence extra work is reduce to copy in new classes
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	//consumer group id is mandator for sepeation of funcationality
	//we could add this in applioacation.yml and will do for all the listeneres
	//@KafkaListener(topics = { "helloworld" } /* , groupId = "consumer-service-01" */)
	public void listenMessage(String message) {
		LOGGER.info("Recieved Message "+message);
	}
	
	//as part of kafka stream we are consuming from strea processed topic
	@KafkaListener(topics = { "helloworld_uppercase" } /* , groupId = "consumer-service-01" */)
		public void listenStreamedMessage(String message) {
			LOGGER.info("Recieved Stream processed Message "+message);
		}
}
