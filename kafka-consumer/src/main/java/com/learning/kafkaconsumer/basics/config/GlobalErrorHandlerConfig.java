package com.learning.kafkaconsumer.basics.config;

import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import com.learning.kafkaconsumer.errorHandler.GlobalErrorHandler;

@Configuration
public class GlobalErrorHandlerConfig {
	
	//@Autowired
	//private ConsumerFactory<Object, Object> consumerFactory;

	@Bean
	public ConcurrentKafkaListenerContainerFactory<Object, Object> gloabalConnectionFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			//moved inside from autowiring
			//any configured component can be passed to @bean method as argument, no need to autowire
			ConsumerFactory<Object, Object> consumerFactory,
			GlobalErrorHandler globalErrorHandler
			){
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory);
		//only single line added, other is same as other config class for near and far car location
		factory.setErrorHandler(globalErrorHandler);
		return factory;
	}
}
