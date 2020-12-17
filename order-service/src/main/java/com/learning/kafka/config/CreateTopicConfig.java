package com.learning.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learning.kafka.OrderServiceConstants;

@Configuration
public class CreateTopicConfig {

	@Bean
	public NewTopic createTopic() {
		return new NewTopic(OrderServiceConstants.ORDER_TOPIC_NAME, 
				OrderServiceConstants.ORDER_TOPIC_PARTITIONS
				, OrderServiceConstants.ORDER_TOPIC_REPLICAS);
	}
	
	@Bean
	public NewTopic createAcknoledgeTopic() {
		return new NewTopic(OrderServiceConstants.ORDER_ACKNOWLEDGE_TOPIC, 
				OrderServiceConstants.ORDER_TOPIC_PARTITIONS
				, OrderServiceConstants.ORDER_TOPIC_REPLICAS);
	}
}
