package com.learning.kafkaconsumer.basics;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class RebalancingTopicMessageConsumer {
	private static final String REBALCNING_TOPIC="rebalancing_topic";
	//if partition is single we wont be  able to use other 2 threads in concurrency to utilize paritions
	//partitions in topic of kafka can be utilized by threads in concurrnecy and hnence both value shud be equal for best performance
	//threads are 3 but partion is 1 and hence other threads wont be used
	@KafkaListener(topics = REBALCNING_TOPIC,concurrency = "3")
	public void receiveFixedRateTopicMessage(ConsumerRecord<String, String> record) {
		System.out.println(String.format("RebalancingTopicMessageConsumer: Recieved Message %s , partition %s ", record.value(),record.partition()));
	}
}
