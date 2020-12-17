package com.learning.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.learning.kafka.messages.OrderAcknowledgeMessage;
import com.learning.kafka.messages.OrderMessage;

@Service
@KafkaListener(topics = OrderServiceConstants.ORDER_TOPIC_NAME)
public class OrderConsumer {

	Logger logger = LoggerFactory.getLogger(this.getClass());

//	@KafkaHandler
//	public void consumeOrder(ConsumerRecord<String, OrderMessage> consumerRecord) {
//		logger.info("Processing OrderMessage {}", consumerRecord.value());
//
//		Header lastHeader = consumerRecord.headers().lastHeader(OrderServiceConstants.SURPRISE_BONUS_HEADER);
//		if (lastHeader != null) {
//			int extraBonus = Integer.valueOf(String.valueOf(lastHeader.value()));
//			logger.info("Applying surprise bonus of {} for OrderMessage {}", extraBonus,consumerRecord.value());
//		}
//	}
	
//	@KafkaHandler
//	public void consumeOrder(OrderMessage message) {
//		logger.info("Processing OrderMessage {}",message);
//
//	}
	
	//with acknoledgement
	@KafkaHandler
	@SendTo(OrderServiceConstants.ORDER_ACKNOWLEDGE_TOPIC)
	public OrderAcknowledgeMessage consumeOrder(OrderMessage message) {
		logger.info("Processing OrderMessage {}",message);
		
		//acknowledgement message
		return new OrderAcknowledgeMessage(message.getOrderId(), message.getOrderNumber(), message.getItemId());

	}
	

}
