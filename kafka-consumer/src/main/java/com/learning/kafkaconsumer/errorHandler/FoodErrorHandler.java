package com.learning.kafkaconsumer.errorHandler;

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;


@Service
public class FoodErrorHandler implements ConsumerAwareListenerErrorHandler{
	Logger logger = LoggerFactory.getLogger(FoodErrorHandler.class);
	@Override
	public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
		
		logger.info("Handling error with error {} due to cause {}, ",message.getPayload(),exception.getMessage());
		//couldbe sending to logstach/kibana etc
		return null;
	}

}
