package com.learning.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.consumers.entities.CarLocation;

//@Configuration
public class NearCarLocationConsumerConfig {

	//consumer do not need kafkatempalte
	
	//not using autowired and auto fetching other bean called kafkaproperties
	@Bean
	public ConsumerFactory<Object, Object> consumerFactory(KafkaProperties kafkaProperties){
		Map<String, Object> buildConsumerProperties = kafkaProperties.buildConsumerProperties();
		buildConsumerProperties.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "120000");
		return new DefaultKafkaConsumerFactory<>(buildConsumerProperties);
	}
	
	
	
	//custom config for near car location factory
	@Bean("nearCarLocationConfigFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> nearCarLocationConfigFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			KafkaProperties kafkaProperties , ObjectMapper objectMapper){
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory(kafkaProperties));
		
		
		RecordFilterStrategy<Object, Object> recordFilterStrategy = new RecordFilterStrategy<Object, Object>(){

			@Override
			public boolean filter(ConsumerRecord<Object, Object> consumerRecord) {
				//we know that it is carlocation always
				try {
				CarLocation carLocation = objectMapper.readValue(consumerRecord.value().toString(), CarLocation.class);
				long distance = carLocation.getDistance();
				return distance >= 0 && distance <= 100;
				//for true process for false remove
				}
				catch (Exception e) {
					//any bad data exception lets block the message for further processing
					return false;
				}
			}
			
		
	};
	
	factory.setRecordFilterStrategy(recordFilterStrategy);
	return factory;
	}
}
