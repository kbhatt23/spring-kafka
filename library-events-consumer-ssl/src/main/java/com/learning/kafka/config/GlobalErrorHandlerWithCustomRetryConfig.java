package com.learning.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.learning.kafka.messages.LibraryEvent;


@Configuration
public class GlobalErrorHandlerWithCustomRetryConfig {

	 @Bean
	    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
	            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
	            ConsumerFactory<Object, Object> kafkaConsumerFactory,
	            GlobalErrorHandler globalErrorHandler) {
	        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
	        configurer.configure(factory, kafkaConsumerFactory);
	        factory.setRetryTemplate(generateRetryTemplate());
	        factory.setErrorHandler(globalErrorHandler);
	        //recovery logic, when retry is excaushted will be claled
	        //we cna check if still recvoerable we can publish to the same topic so that it can be consumed later
	        //if item is not recoverable we publish to deda letter topic
	        factory.setRecoveryCallback(context ->{
	        	if(context.getLastThrowable().getCause() instanceof RecoverableDataAccessException) {
	        		//recoverable case, publish to topic again
	        		System.out.println("Global config recovery method called for recoverable items ");
	        		
//	        		Arrays.stream(context.attributeNames())
//	        			.forEach(name -> System.out.println("attribute found with name "+name+" and value "+context.getAttribute(name)));
	        	
	        		//we can take the record and manually use kafka template to send event to oroginal topic
	        		//however @sendto and recoverer handle in connectin factory are better choic for succes and failure topic communication
	        		ConsumerRecord<String, LibraryEvent> record = (ConsumerRecord<String, LibraryEvent>) context.getAttribute("record");
	        	
	        		String topic = LibraryEventsConstants.LIBRARY_TOPIC_NAME;
	        		String key = record.key();
	        		LibraryEvent value = record.value();
	        		//kafkaTemplate.send(topic,key,value);
	        	}else {
	        		//non-recoverable case, publish to dead letter topic
	        		System.out.println("Global config recovery method called for non-recoverable items ");
	        		ConsumerRecord<String, LibraryEvent> record = (ConsumerRecord<String, LibraryEvent>) context.getAttribute("record");
		        	
	        		String topic = "library-deda-letter-topic";
	        		String key = record.key();
	        		LibraryEvent value = record.value();
	        		//kafkaTemplate.send(topic,key,value);
	        	}
	        	return null;
	        });
	        
	        return factory;
	    }
//	 private RetryTemplate generateRetryTemplate() {
//			RetryTemplate template = new RetryTemplate();
//			Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<Class<? extends Throwable>, Boolean>();
//			retryableExceptions.put(IllegalArgumentException.class, false);
//			retryableExceptions.put(RuntimeException.class, false);
//			//retry true only for speciic exception
//			retryableExceptions.put(RecoverableDataAccessException.class, true);
//			
//			template.setRetryPolicy(new SimpleRetryPolicy(3, retryableExceptions,true ));
//			FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
//			//backoff of 10 seconds
//			fixedBackOffPolicy.setBackOffPeriod(1000);
//			template.setBackOffPolicy(fixedBackOffPolicy);
//			return template;
//		}
	 
	 private RetryTemplate generateRetryTemplate() {

	        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
	        fixedBackOffPolicy.setBackOffPeriod(1000);
	        RetryTemplate retryTemplate = new RetryTemplate();
	        retryTemplate.setRetryPolicy(simpleRetryPolicy());
	        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
	        return  retryTemplate;
	    }

	    private RetryPolicy simpleRetryPolicy() {

	        /*SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
	        simpleRetryPolicy.setMaxAttempts(3);*/
	        Map<Class<? extends Throwable>, Boolean> exceptionsMap = new HashMap<>();
	        exceptionsMap.put(IllegalArgumentException.class, false);
	        exceptionsMap.put(RecoverableDataAccessException.class, true);
	        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3,exceptionsMap,true);
	        return simpleRetryPolicy;
	    }
	 
}
