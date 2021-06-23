package com.learning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import com.learning.model.Employee;
import com.learning.model.Order;

@SpringBootApplication
@EnableBinding({Sink.class,CustomConsumerSource.class,OrderConsumerSource.class,ErrorRecoverableChannelSource.class,
	ErrorDeadLetterChannelSink.class,DeadLetterSource.class}) // acting as listener
//also creates queue and exchange with specific name
//input should be the exchange name and queue name will be random group id.queue random string
public class StreamConsumerApplication {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(StreamConsumerApplication.class, args);
	}
	
	//use exchane name as argument and not queue name
	@StreamListener(Sink.INPUT)
	public void listenEmployee(@Payload Employee emp) {
		
		logger.info("listenEmployee: processing employee "+emp);
	}
	
	//lets use channel name and not queue name
	//use chnalle name and not queueu name and exchange and channel name is same
	@StreamListener(CustomConsumerSource.CHANNEL)
	public void customEmployee(@Payload Employee emp) {
		
		logger.info("customEmployee: processing employee "+emp);
	}
	
	@StreamListener(OrderConsumerSource.CHANNEL)
public void orders(@Payload Order order) {
		
		logger.info("orders: processing order "+order);
	}
	
	//channle.exchange name and not queue name
	@StreamListener(ErrorRecoverableChannelSource.CHANNEL)
	public void errorRecoveryChannel(@Payload Employee employee) {
		logger.info("errorRecoveryChannel: start processing employee "+employee);
		
		//lets manually throw exception
		//retry with fallback for 3 times then go to error channel -> destinaton.queue.error name will be default
		boolean failed=true;
		if(failed)
			throw new IllegalStateException("errorRecoveryChannel: Can not handle employee data");
		logger.info("errorRecoveryChannel: completed processing employee "+employee);
		
	}
	
	@ServiceActivator(inputChannel = "error-recovery.queue.errors" /*
																	 * , outputChannel =
																	 * DeadLetterSource.OUTPUT
																	 */)
	//other than output channel we can use @sendto
	@SendTo(value =DeadLetterSource.OUTPUT )
	public Message<?> handleErrorFromSpecificChannelOOB(Message<?> message) {
		logger.info("handleErrorFromSpecificChannelOOB: handling error payload: {} ,  with headers: {}"+message.getPayload(),message.getHeaders());
		return message;
		}
	
	
	@StreamListener(ErrorDeadLetterChannelSink.CHANNEL)
	public void deadLEtterMessage(Message<?> message) {
		logger.info("deadLEtterMessage: handling dead letter payload: {} ,  with headers: {}"+message.getPayload(),message.getHeaders());
	}
	
}
