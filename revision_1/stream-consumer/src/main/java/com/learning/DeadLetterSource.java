package com.learning;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;

public interface DeadLetterSource {


	/**
	 * Name of the output channel.
	 */
	String OUTPUT = "deadLetterSource";

	/**
	 * @return output channel
	 */
	@Output(DeadLetterSource.OUTPUT)
	MessageChannel output();


}
