package com.learning.kafkaconsumer.basics.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;

@Configuration
public class DeadLetterTopicConfig {

	@Bean("deadLetterContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> deadLetterConfig(ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			//moved inside from autowiring
			//any configured component can be passed to @bean method as argument, no need to autowire
			ConsumerFactory<Object, Object> consumerFactory,
			KafkaTemplate<Object, Object> kafkaTemplate
			){
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory);
		
		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate, (record,ex) ->
			 new TopicPartition("invoice-dead-topic", record.partition())
		);
			
		SeekToCurrentErrorHandler seekToCurrentErrorHandler = new SeekToCurrentErrorHandler(recoverer, 4);
		factory.setErrorHandler(seekToCurrentErrorHandler);		
		return factory;
	}

}
