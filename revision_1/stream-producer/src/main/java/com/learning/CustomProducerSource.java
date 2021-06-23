package com.learning;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;

//along with oob source lets create custom producer
public interface CustomProducerSource {

	/**
	 * Name of the output channel.
	 */
	String CHANNEL = "custom-channel";

	/**
	 * first prioerity is annotation argumnent if empty method name will be exchnage/channel name
	 */
	@Output(CustomProducerSource.CHANNEL)
	MessageChannel output();
}
