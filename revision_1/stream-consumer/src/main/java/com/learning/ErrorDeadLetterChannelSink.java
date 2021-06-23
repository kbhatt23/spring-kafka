package com.learning;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ErrorDeadLetterChannelSink {
	//exhange name will be destination name
	//queue name will be desination.group
	//error internal channel name will be destinaltion.grouo.error
	//this error channel wont have exchange and queue present meaning no DLX bvy default
	//so we need to handle it manually in code
	String CHANNEL = "deadLetterChannel";
	
	@Input(CHANNEL)
	SubscribableChannel input();
}
