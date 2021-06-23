package com.learning.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.messages.OrderMessage;
import com.learning.messages.OrderMessageBulkQuantity;

@Service
public class StreamTransformUtil {

	@Autowired
	private ObjectMapper objectMapper;
	
	public  <T> T readValueFromString(String message , Class<T> instanceType) {
		T messageObj  = null;
		try {
			messageObj = objectMapper.readValue(message, instanceType);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		return messageObj;
	}
	
	public   String writeToJsonString(Object object ) {
		String jsonStr  = null;
		try {
			jsonStr = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		return jsonStr;
	}
	
	//for bulk orders we calcualte total price
	//for non bulk order we directly push to different topic
	public  boolean isOrderBulk(OrderMessage order) {
		return order.getQuantity() > 100;  // 100 is bulk
	}
	
	public OrderMessageBulkQuantity bulkTransform(OrderMessage order) {
		OrderMessageBulkQuantity orderMessageBulkQuantity = new OrderMessageBulkQuantity();
		orderMessageBulkQuantity.setOrderId(order.getOrderId());
		orderMessageBulkQuantity.setOrderNumber(order.getOrderNumber());
		orderMessageBulkQuantity.setItemId(order.getItemId());
		orderMessageBulkQuantity.setAddress(order.getAddress());
		orderMessageBulkQuantity.setCardNumber(maskCard(order.getCardNumber())); //mast this also
		orderMessageBulkQuantity.setCreationDate(order.getCreationDate());
		orderMessageBulkQuantity.setItemName(order.getItemName());
		orderMessageBulkQuantity.setLastModifiedDate(order.getLastModifiedDate());
		orderMessageBulkQuantity.setTotalBulkPrice(order.getPrice() * order.getQuantity());
		orderMessageBulkQuantity.setQuantity(order.getQuantity());
		//see we have removed unit price and quantity reamin untacked
		
		return orderMessageBulkQuantity;
		
	}
	public OrderMessageBulkQuantity nonBulkTransform(OrderMessage order) {
		OrderMessageBulkQuantity orderMessageBulkQuantity = new OrderMessageBulkQuantity();
		orderMessageBulkQuantity.setOrderId(order.getOrderId());
		orderMessageBulkQuantity.setOrderNumber(order.getOrderNumber());
		orderMessageBulkQuantity.setItemId(order.getItemId());
		orderMessageBulkQuantity.setAddress(order.getAddress());
		orderMessageBulkQuantity.setCardNumber(maskCard(order.getCardNumber())); //mast this also
		orderMessageBulkQuantity.setCreationDate(order.getCreationDate());
		orderMessageBulkQuantity.setItemName(order.getItemName());
		orderMessageBulkQuantity.setLastModifiedDate(order.getLastModifiedDate());
		orderMessageBulkQuantity.setPrice(order.getPrice());
		orderMessageBulkQuantity.setQuantity(order.getQuantity());
		
		//for non bulk totatl price is 0 and price is unit price
		//for bulk unit price is 0 anb total price is n*m
		
		return orderMessageBulkQuantity;
		
	}
	
	
	
	public String maskCard(String cardNumber) {
		int maskLength =4;
		StringBuilder maskedChars = new StringBuilder();
		for(int i =0 ; i < 4; i++) {
			maskedChars.append("*");
		}
		//mask all excpet last 4 chars
		cardNumber = cardNumber.substring(0, cardNumber.length()-maskLength) + maskedChars.toString();
		return cardNumber;
	}
}
