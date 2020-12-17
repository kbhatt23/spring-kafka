package com.learning.kafka.brokers;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.learning.kafka.OrderServiceConstants;
import com.learning.kafka.messages.OrderMessage;
@Service
public class OrderMessageProducer {

	@Autowired
	private KafkaTemplate<String, OrderMessage> kafkaTemplate;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderMessage.class);
	
	//adding header
	public ProducerRecord<String, OrderMessage> createProducerRecordWithHeader(OrderMessage orderMessage){
		int additionalBonus=20;
		
		
		List<Header> headers = new ArrayList<Header>();
		RecordHeader header = new RecordHeader(OrderServiceConstants.SURPRISE_BONUS_HEADER, String.valueOf(additionalBonus).getBytes());
		headers.add(header);
		
		return new ProducerRecord<>(OrderServiceConstants.ORDER_TOPIC_NAME, 
				null, String.valueOf(orderMessage.getOrderId()), orderMessage, headers);
		
		
	}
	
	public void produceOrderMessage(OrderMessage orderMessage) {
		//orderId as key, so that partition order is maintained
		//ListenableFuture<SendResult<String, OrderMessage>> futureSend = kafkaTemplate.send(OrderServiceConstants.ORDER_TOPIC_NAME,
			//	String.valueOf(orderMessage.getOrderId()), orderMessage);
		ProducerRecord<String, OrderMessage> producerRecord = createProducerRecordWithHeader(orderMessage);
		ListenableFuture<SendResult<String, OrderMessage>> futureSend = kafkaTemplate.send(producerRecord);
		
		//non blocking async code , we cna use callback to confirm if mesage was sent or failed later
		futureSend.addCallback(new ListenableFutureCallback<SendResult<String, OrderMessage>>() {

			@Override
			public void onSuccess(SendResult<String, OrderMessage> result) {
				
				LOGGER.info("produceOrderMessage: Order {} successfully sent to Kafka ",orderMessage);
			}

			@Override
			public void onFailure(Throwable ex) {
				LOGGER.error("produceOrderMessage: Order {} failed while sending to Kafka ",orderMessage);				
			}
		});
		
	}
}
