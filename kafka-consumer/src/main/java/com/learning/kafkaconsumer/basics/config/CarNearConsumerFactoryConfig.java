package com.learning.kafkaconsumer.basics.config;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaconsumer.basics.entity.CarLocation;

@Configuration
public class CarNearConsumerFactoryConfig {
	@Autowired
	private KafkaProperties kafkaProperties;

	@Bean
	public ConsumerFactory<Object, Object> consumerFactory() {
		 Map<String, Object> properties = kafkaProperties.buildConsumerProperties();

		properties.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "120000");

		return new DefaultKafkaConsumerFactory<Object, Object>(properties);
	}
	
	@Autowired
	private ObjectMapper objectMapper;

	//asuming string as key and value for carlocation topic
	@Bean("carNearConsumerFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> carNearConsumerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer){
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory());
		RecordFilterStrategy<Object, Object> recordFilterStrategy = consumerRecord -> {
			
			try {
				CarLocation carLocation = objectMapper.readValue(consumerRecord.value().toString(), CarLocation.class);
				//true in case of skipping condition
				if(carLocation.getDistance() > 100) {
					//for more than 100 return true and ignore it
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//in case 100 or less or erro condition we do not filter it and let consumer group handle it
			return false;
		};
		factory.setRecordFilterStrategy(recordFilterStrategy);
		return factory;
	}
	
	@Bean("carFarConsumerFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> carFarConsumerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer){
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory());
		RecordFilterStrategy<Object, Object> recordFilterStrategy = consumerRecord -> {
			
			try {
				CarLocation carLocation = objectMapper.readValue(consumerRecord.value().toString(), CarLocation.class);
				if(carLocation.getDistance() <= 100) {
					//for more than 100 return true and ignore it
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		};
		factory.setRecordFilterStrategy(recordFilterStrategy);
		return factory;
	}
	
	
	
}
