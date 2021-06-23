package com.learning;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

//along with oob source lets create custom producer
public interface CustomConsumerSource {


	/**
	 * Input channel name.
	 */
	String CHANNEL = "custom-channel";

	/**
	 * @return input channel.
	 */
	@Input(CustomConsumerSource.CHANNEL)
	SubscribableChannel input();


}
