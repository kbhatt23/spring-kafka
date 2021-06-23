package com.learning.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.learning.messages.WebColorVoteMessage;

@Service
public class WebColorVoteProducer {

	@Autowired
	private KafkaTemplate<String, WebColorVoteMessage> kafkaTemplate;

	public void publish(WebColorVoteMessage message) {
		message.setVoteDateTime(LocalDateTime.now());
		kafkaTemplate.send("colorvote", message.getUsername(), message);
	}

}