package com.learning.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.messages.OnlineOrderPaymentMessage;

//@Service
public class OrderPaymentJoinConsumer {

	
	@KafkaListener(topics = {"order-payment-inner"})
	public void handlePaymentOrderInnerJoin(ConsumerRecord<String, OnlineOrderPaymentMessage> record) {
		System.out.println("handlePaymentOrderInnerJoin: handling order-payment message "+record.value()+" for id "+record.key());
	}
}
