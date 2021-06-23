package com.learning.producers;

import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.producers.entities.InnerNonRelatableEntity;
import com.learning.producers.entities.NonRelatableEntity;

//scheduled producer
//if this is enabled the application keeps on running
//@Service
public class JacksonJSONProducer implements CommandLineRunner{

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private static final String TOPIC_NAME = "jsontopic";
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ObjectMapper objectMapper;
	

	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("Sending Custom JSON messages");
		
		//from 1 to 90
		IntStream.rangeClosed(1, 10)
				.mapToObj(i -> 
					new NonRelatableEntity("entity-"+i, "description for entity "+i, 
							new InnerNonRelatableEntity(i))
				)//convert to key string
				.forEach(entity ->{
					String entityJSONStr;
					try {
						entityJSONStr = objectMapper.writeValueAsString(entity);
						//paritioning based on odd or even
						int keyInt = Integer.valueOf(entity.getName().split("-")[1]);
						String key = keyInt % 2 ==0 ? "1": "2";
						kafkaTemplate.send(TOPIC_NAME,key, entityJSONStr);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				});
	}

	
}
