package com.learning.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.learning.messages.WebLayoutVoteMessage;

@Service
public class WebLayoutVoteProducer {

	@Autowired
	private KafkaTemplate<String, WebLayoutVoteMessage> kafkaTemplate;

	public void publish(WebLayoutVoteMessage message) {
		message.setVoteDateTime(LocalDateTime.now());
		kafkaTemplate.send("layoutvote", message.getUsername(), message);
	}

}
