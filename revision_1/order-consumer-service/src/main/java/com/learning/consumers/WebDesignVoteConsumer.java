package com.learning.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.messages.WebDesignVoteMessage;

@Service
public class WebDesignVoteConsumer {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@KafkaListener(topics = "vote-final" , groupId = "big-data")
	public void webDesignVote(ConsumerRecord<String, WebDesignVoteMessage> record) {
		//automatically it can be deserialized
		//however package name of ordermessage.java file should be same as that in producer and processor api
		logger.info("webDesignVote: Recieved rating feedback "+record.value()+" with key "+record.key());
	}
	
	
}
