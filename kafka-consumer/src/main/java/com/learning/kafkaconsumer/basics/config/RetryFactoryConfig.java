package com.learning.kafkaconsumer.basics.config;

import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryFactoryConfig {

	@Bean("retryFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> gloabalConnectionFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			//moved inside from autowiring
			//any configured component can be passed to @bean method as argument, no need to autowire
			ConsumerFactory<Object, Object> consumerFactory
			){
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory);
		factory.setRetryTemplate(generateRetryTemplate());
		
		return factory;
	}

	private RetryTemplate generateRetryTemplate() {
		RetryTemplate template = new RetryTemplate();
		template.setRetryPolicy(new SimpleRetryPolicy(3));
		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		//backoff of 10 seconds
		fixedBackOffPolicy.setBackOffPeriod(10000);
		template.setBackOffPolicy(fixedBackOffPolicy);
		return template;
	}
	
}
