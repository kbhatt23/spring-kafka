package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.messages.ItemFeedBackResult;

@Service
public class AverageRatingFeedbackConsumer {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@KafkaListener(topics = "itemFeedBack-result" , groupId = "big-data")
	public void averageRAtingFeedBack(ItemFeedBackResult record) {
		//automatically it can be deserialized
		//however package name of ordermessage.java file should be same as that in producer and processor api
		logger.info("averageRAtingFeedBack: Recieved rating feedback "+record);
	}
	
	
}
