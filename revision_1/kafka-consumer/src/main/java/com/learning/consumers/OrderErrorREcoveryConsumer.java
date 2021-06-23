package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.consumers.entities.OrderEntity;
import com.learning.consumers.entities.SimpleNumber;

//@Service
public class OrderErrorREcoveryConsumer {

	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@KafkaListener(topics = {"ordertopic"} , errorHandler = "simpleErrorRecoveryHandler")
	public void processOrder(String orderStr) {
		OrderEntity order = null;
		try {
			order = objectMapper.readValue(orderStr, OrderEntity.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		logger.info("starting processing of order "+order);
		//in kafka even though we throw exception it continues updating offset until we say to retry
		//since amnt is negative no retuy will work so lets just trhrow exception and continue
		if(order.getAmount() < 0d) {
			throw new IllegalStateException("order "+order.getOrderID()+" have negative amount "+order.getAmount());
		}
		
		//update consumer offset
		logger.info("succesfuly processed order "+order);
		
	}
	//no error handler customized
	//so excpetion will have no error handler called in case no global error handler exists
	@KafkaListener(topics = {"simplenumbertopic"} )
	public void processNumber(String orderStr) {
		SimpleNumber readValue= null;
		try {
			readValue = objectMapper.readValue(orderStr, SimpleNumber.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logger.info("starting processing number"+readValue);
		
		if(readValue.getId() % 2 != 0) {
			//automatically offset will be updated
			//but since no error hadnler exist it prints big exception stack and continue with next element offset
			throw new IllegalStateException("can not handle odd item "+readValue);
		}
		
		logger.info("completed processing number"+readValue);
	}
	
}
