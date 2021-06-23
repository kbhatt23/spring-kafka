package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.messages.Discount;
import com.learning.messages.Promotion;

@Service
@KafkaListener(topics = { "promotion-discount" })
//same topic can hold any type of string, could be class A or class b object
public class PromotionDiscuntCombinedCosumer {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@KafkaHandler
	public void consumePromotion(Promotion promotion) {
		logger.info("processing promotion " + promotion);
	}

	@KafkaHandler
	public void consumeDisount(Discount discount) {
		logger.info("processing discount " + discount);
	}
	
	//default in case in same topic an instance is passed other than other kafkahandlers defined
	@KafkaHandler(isDefault = true)
	public void consumeGenericObject(Object object) {
		logger.info("processing generic object " + object);
	}

}
