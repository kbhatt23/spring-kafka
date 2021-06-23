package com.learning;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

//along with oob source lets create custom producer
public interface OrderConsumerSource {


	/**
	 * Input channel name.
	 */
	String CHANNEL = "order-channel";

	/**
	 * @return input channel.
	 */
	@Input(OrderConsumerSource.CHANNEL)
	SubscribableChannel input();


}
