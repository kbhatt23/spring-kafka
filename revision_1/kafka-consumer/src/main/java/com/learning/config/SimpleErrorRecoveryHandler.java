package com.learning.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

//we can use this bean in any kafka listener
//anyway kafka do not retry for any exception and continue processing further messages and upadte the offset
//using this we can handle such error in consumer and gracefully log to kibana or do something useful with it
//@Service
public class SimpleErrorRecoveryHandler implements ConsumerAwareListenerErrorHandler{

	Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
		logger.warn("saving exception logs in kibana for message {} , with exception {}",message.getPayload(), exception.getMessage());
		return null;
	}

}
