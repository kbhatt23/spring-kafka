package com.learning.kafkaconsumer.basics;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaconsumer.basics.entity.Commodity;

//@Service
public class CommodityNotificationConsumerGroup {

	Logger logger = LoggerFactory.getLogger(CommodityNotificationConsumerGroup.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	//groupid i mandatory , we gave deafault value in case it is not present
	//same topic can have multiple groupos whihc makes each group indepenedt -> one can be slower but wont impact other
	@KafkaListener(topics = "commodity_groups",groupId = "commodity-notification")
	public void receiveFixedRateTopicMessage(String message) {
		Commodity commodity;
		try {
			commodity = objectMapper.readValue(message, Commodity.class);
			logger.info("Recieving message for notification mapping "+commodity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Thread.sleep(1000);
	}
}
