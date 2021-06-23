package com.learning.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class GoodFeedbackConsumer {

	Logger logger = LoggerFactory.getLogger(getClass());
private static final String OUTPUT_BAD_TOPIC_COUNT = "bad-feedback-count";
	
	private static final String OUTPUT_TOPIC_COUNT = "good-feedback-count";
	
	@KafkaListener(topics = "good-feedback" , groupId = "big-data")
	public void bigDataFeedback(ConsumerRecord<String,String> record) {
		//automatically it can be deserialized
		//however package name of ordermessage.java file should be same as that in producer and processor api
		logger.info("bigDataFeedback: Recieved good feedback word "+record.value()+" with key "+record.key());
	}
	
	@KafkaListener(topics = "bad-feedback" , groupId = "big-data")
	public void bigDataBadFeedback(ConsumerRecord<String,String> record) {
		//automatically it can be deserialized
		//however package name of ordermessage.java file should be same as that in producer and processor api
		logger.info("bigDataBadFeedback: Recieved bad feedback word "+record.value()+" with key "+record.key());
	}
	
	//count words good and bad
	
	@KafkaListener(topics = OUTPUT_BAD_TOPIC_COUNT , groupId = "big-data")
	public void bigDataBadFeedbackCount(ConsumerRecord<String,String> record) {
		//automatically it can be deserialized
		//however package name of ordermessage.java file should be same as that in producer and processor api
		logger.info("bigDataBadFeedbackCount: Recieved bad feedback word count "+record.value()+" with key "+record.key());
	}
	
	@KafkaListener(topics = OUTPUT_TOPIC_COUNT , groupId = "big-data")
	public void bigDataGoodFeedbackCount(ConsumerRecord<String,String> record) {
		//automatically it can be deserialized
		//however package name of ordermessage.java file should be same as that in producer and processor api
		logger.info("bigDataGoodFeedbackCount: Recieved good feedback word count "+record.value()+" with key "+record.key());
	}
	
}
