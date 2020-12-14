package com.learning.kafkaproducer.basics;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaproducer.basics.entity.Employee;

//@Component
class JacksonAsStringProducerRunner implements CommandLineRunner {

	private static final String MULTI_PARITION_TOPIC = "json-as-string-partition";

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void run(String... args) throws Exception {
		List<Employee>  employees = IntStream.rangeClosed(1,5)
			.mapToObj(i -> new Employee("employee-"+i, 99.99+i, LocalDate.now()))
			.collect(Collectors.toList())
			;
		
		employees.stream()
				.map(t -> {
					try {
						return objectMapper.writeValueAsString(t);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "";
				})
				.map(empStr -> kafkaTemplate.send(MULTI_PARITION_TOPIC,empStr))
				.forEach(empStr -> System.out.println("Message transformed : "+empStr));
				;
		//let kafka decide partitioning rule , no need of key
	}

}