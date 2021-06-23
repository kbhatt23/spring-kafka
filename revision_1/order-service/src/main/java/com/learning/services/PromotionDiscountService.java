package com.learning.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.learning.messages.Discount;
import com.learning.messages.OrderMessage;
import com.learning.messages.Promotion;

//both json sent to same topic
@Service
public class PromotionDiscountService {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	public void sendDiscount(Discount discount) {
		kafkaTemplate.send("promotion-discount", String.valueOf(discount.getDiscountID()), discount);
	}
	
	public void sendPromotion(Promotion promotion) {
		kafkaTemplate.send("promotion-discount", String.valueOf(promotion.getPromotionID()), promotion);
	}
	
	public void sendGenericObject(Object object) {
		kafkaTemplate.send("promotion-discount", object);
	}
	
	//used by order controller
	public void sendTestableOrderMessage(OrderMessage orderMessage) {
		kafkaTemplate.send("promotion-discount", orderMessage.getOrderNumber(), orderMessage);
	}
}
