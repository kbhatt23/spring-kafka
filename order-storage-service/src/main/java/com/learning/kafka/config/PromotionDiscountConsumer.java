package com.learning.kafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.kafka.messages.DiscountMessage;
import com.learning.kafka.messages.PromotionMessage;

@Service
@KafkaListener(topics = OrderServiceConstants.PROMOTION_DISCOUNT_TOPIC)
public class PromotionDiscountConsumer {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@KafkaHandler
	public void consumeDiscount(DiscountMessage message) {
		logger.info("Processing discountMessage {}",message);
	}
	
	@KafkaHandler
	public void consumePromotion(PromotionMessage message) {
		logger.info("Processing promotionMessage {}",message);
	}
}
