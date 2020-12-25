package com.learning.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CreateTopicConfig {

	@Bean
	public NewTopic createTopic() {
		return new NewTopic(LibraryEventsConstants.LIBRARY_TOPIC_NAME, 
				LibraryEventsConstants.LIBRARY_TOPIC_PARTITIONS
				, LibraryEventsConstants.LIBRARY_TOPIC_REPLICAS);
	}
	
//	@Bean
//	public NewTopic createAcknoledgeTopic() {
//		return new NewTopic(OrderServiceConstants.ORDER_ACKNOWLEDGE_TOPIC, 
//				OrderServiceConstants.ORDER_TOPIC_PARTITIONS
//				, OrderServiceConstants.ORDER_TOPIC_REPLICAS);
//	}
}
