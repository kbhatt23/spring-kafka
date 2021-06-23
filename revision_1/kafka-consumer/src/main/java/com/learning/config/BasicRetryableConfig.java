package com.learning.config;

import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class BasicRetryableConfig {

	@Bean("basicRetryableContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> basicRetryableContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			// moved inside from autowiring
			// any configured component can be passed to @bean method as argument, no need
			// to autowire
			ConsumerFactory<Object, Object> consumerFactory, GlobalErrorHandler globalErrorHandler) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory);
		// only single line added, other is same as other config class for near and far
		// car location
		factory.setErrorHandler(globalErrorHandler);
		factory.setRetryTemplate(generateRetryTemplate());
		// retryable tempalte

		// we can set gloabal retry here
		// factory.setRetryTemplate(retryTemplate);
		return factory;
	}

	private RetryTemplate generateRetryTemplate() {
		RetryTemplate template = new RetryTemplate();
		template.setRetryPolicy(new SimpleRetryPolicy(3));
		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		// backoff of 5 seconds
		fixedBackOffPolicy.setBackOffPeriod(5000);
		template.setBackOffPolicy(fixedBackOffPolicy);
		return template;
	}
}
