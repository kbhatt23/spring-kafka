package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learning.messages.WebColorVoteMessage;
import com.learning.messages.WebLayoutVoteMessage;
import com.learning.services.WebColorVoteProducer;
import com.learning.services.WebLayoutVoteProducer;

@RestController
public class VoteColorLayoutController {

	@Autowired
	private WebColorVoteProducer colorProducer;
	
	@Autowired
	private WebLayoutVoteProducer layoutProducer;
	
	@PostMapping("/vote-color")
	public void voteColor(@RequestBody WebColorVoteMessage message) {
		colorProducer.publish(message);
	}
	
	@PostMapping("/vote-layout")
	public void voteLayout(@RequestBody WebLayoutVoteMessage message) {
		layoutProducer.publish(message);
	}
}
