package com.learning.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.learning.avro.Avro01;
import com.learning.avro.Employee;

//@Service
public class EmployeeAvroConsumer {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//@KafkaListener(topics = {"employees-topic"})
	public void consumeEmployee(Employee obj) {
		logger.info("consumeEmployee: Consuming avro employee "+obj);
	}
	
	@KafkaListener(topics = {"employee-transformed-topic"})
	public void consumeTransformedEmployee(Employee obj) {
		logger.info("consumeTransformedEmployee: Consuming trasnformed avro employee "+obj);
	}
}
