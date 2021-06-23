package com.learning.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class ConcurrentConsumer {

	//not making it static just to allow freedom on logger name
	//in statis getClass wont work and hence extra work is reduce to copy in new classes
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	//literal has to be static and final -> actual constant or string literal
	private static final String TOPIC_NAME = "multipartitionedtopic";
	
	

	//the value to annotation should be literal or constant
	@KafkaListener(topics = { TOPIC_NAME } /* , groupId = "consumer-service-01" */
	,concurrency = "3" //we have 3 paritions so we can have one thread linked to each parition
			)
	public void listenMessage(ConsumerRecord<String,String> record) {
		String message= record.value();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("Recieved Message "+message +" by partition "+record.partition());
	}
}
