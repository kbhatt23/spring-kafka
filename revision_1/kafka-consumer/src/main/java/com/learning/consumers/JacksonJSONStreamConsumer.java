package com.learning.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.consumers.entities.NonRelatableEntity;

//@Service
public class JacksonJSONStreamConsumer {

	//not making it static just to allow freedom on logger name
	//in statis getClass wont work and hence extra work is reduce to copy in new classes
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	//literal has to be static and final -> actual constant or string literal
	private static final String TOPIC_NAME = "jsontopic-stream";
	
	@Autowired
	private ObjectMapper objectMapper;

	//the value to annotation should be literal or constant
	@KafkaListener(topics = { TOPIC_NAME } /* , groupId = "consumer-service-01" */
			)
	public void listenMessage(ConsumerRecord<String,String> record) {
		String message= record.value();
		NonRelatableEntity messageObj  = null;
		try {
			messageObj = objectMapper.readValue(message, NonRelatableEntity.class);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOGGER.info("listenMessage1:Recieved Message "+messageObj +" by partition "+record.partition());
	}
	
	
	//this listener is another functionality for same message on same partition
	//could exist in the same service or could be on another service
	//both consumer group is ike different fnctonality itself
	//same messag eon same aprition will go to both of these consumers as part of different consumer group
		@KafkaListener(topics = { TOPIC_NAME }  , groupId = "consumer-service-02" 
				)
		public void listenMessage2(ConsumerRecord<String,String> record) {
			String message= record.value();
			NonRelatableEntity messageObj  = null;
			try {
				messageObj = objectMapper.readValue(message, NonRelatableEntity.class);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LOGGER.info("listenMessage2: Recieved Message "+messageObj +" by partition "+record.partition());
		}
}
