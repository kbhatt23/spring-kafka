package com.learning.kafka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.learning.kafka.config.LibraryEventsConstants;
import com.learning.kafka.messages.LibraryEvent;

@Service
public class LibraryEventProducer {

	@Autowired
	private KafkaTemplate<String, LibraryEvent> kafkaTemplate; 
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void sendLibraryEvent(LibraryEvent event) {
	ListenableFuture<SendResult<String, LibraryEvent>> future = kafkaTemplate.send(LibraryEventsConstants.LIBRARY_TOPIC_NAME, event.getBook().getBookId(), event);
		
		future.addCallback(new ListenableFutureCallback<SendResult<String, LibraryEvent>>() {

			@Override
			public void onSuccess(SendResult<String, LibraryEvent> result) {
				logger.info("sendLibraryEvent: Library event {} succesfully sent to Kafka Topic {}",event,result.getRecordMetadata().topic()+"-"+result.getRecordMetadata().partition());
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.info("sendLibraryEvent: Library event {} failed for Kafka Topic {} with exception {}",event,LibraryEventsConstants.LIBRARY_TOPIC_NAME,ex.getMessage());
			}
		});
	}
}
