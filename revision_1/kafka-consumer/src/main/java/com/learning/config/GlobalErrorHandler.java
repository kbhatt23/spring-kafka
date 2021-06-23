package com.learning.config;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ConsumerAwareErrorHandler;
import org.springframework.stereotype.Service;

@Service
public class GlobalErrorHandler implements ConsumerAwareErrorHandler{
	Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);
	
	@Override
	public void handle(Exception thrownException, ConsumerRecord<?, ?> data, Consumer<?, ?> consumer) {

		//global error handling for kibana/logstach
		logger.info("handling global error for: {}  because of exception: {}",data.value(),thrownException.getMessage());
	}

}