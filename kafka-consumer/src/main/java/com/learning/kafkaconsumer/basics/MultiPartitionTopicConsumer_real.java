package com.learning.kafkaconsumer.basics;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class MultiPartitionTopicConsumer_real {

	Logger logger = LoggerFactory.getLogger(MultiPartitionTopicConsumer_real.class);
	
	//in logs we can see logs getting printed by multiple threads running concurrenctly
	@KafkaListener(topics = "multi-partition-topic",concurrency = "3")
	public void receiveFixedRateTopicMessage(ConsumerRecord<String, String> messageRecord) throws InterruptedException {
		logger.info("receiveFixedRateTopicMessage: Message recieved {} with key {} and partition {}",messageRecord.value(),messageRecord.key(),messageRecord.partition());
		Thread.sleep(1000);
	}
	//concurrnecy works even normally
	//but we wont be able to print form which partition it came, kafka wud have done partitioning base on value of key, but we wont be able to print in logs
//	@KafkaListener(topics = "multi-partition-topic",concurrency = "3")
//	public void receiveFixedRateTopicMessage(String message) throws InterruptedException {
//		logger.info("receiveFixedRateTopicMessage: Message recieved {}",message);
//		Thread.sleep(1000);
//	}
}
