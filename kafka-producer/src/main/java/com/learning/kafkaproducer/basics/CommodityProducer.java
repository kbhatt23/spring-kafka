package com.learning.kafkaproducer.basics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaproducer.basics.entity.Commodity;

//@Service
public class CommodityProducer {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private int counter=1;
	
	@Scheduled(fixedRate = 5000)
	public void scheduleCommodity() {
		Commodity commodity = new Commodity("radhe-krishna:"+counter, Math.random());
		try {
			kafkaTemplate.send("commodity_groups", objectMapper.writeValueAsString(commodity));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		counter++;
	}
}
