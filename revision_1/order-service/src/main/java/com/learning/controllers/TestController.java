package com.learning.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.messages.FakeMessage;

@RestController
@RequestMapping("/test")
public class TestController {
	
	private Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private KafkaTemplate<String, FakeMessage> tempalte;

	@GetMapping
	public String test() {
		String threadName = Thread.currentThread().getName();
		logger.info("Test started by Thread "+threadName);
		
		//force current thread to wait
				//then only we can demostaret other client threads in servelet pool gets blocked
		sleep(10000);
		
		logger.info("Test completed by Thread "+threadName);
		return "jai shree ram says "+threadName;
	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping
	public void testFakeMEssage(@RequestBody FakeMessage message) {
		tempalte.send("fake-test", message.getName(), message);
	}
}
