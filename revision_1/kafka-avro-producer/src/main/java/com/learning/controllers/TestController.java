package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.avro.Avro01;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private KafkaTemplate<String, Avro01> template;
	
	@Autowired
	private KafkaTemplate<String, Object> errorTemplate;

	@GetMapping
	public String publishAvro() {
		Avro01 obj = Avro01.newBuilder().setActive(true)
			.setFullName("Radha Krishna")
			.setMaritalStatus("MARRIED")
			.build();
		
		template.send("avro-01-topic", obj);
		
		return "jai radhe krisha";
	}
	
//	@GetMapping("/error")
//	public String publishErrorAvro() {
//		Employee employee = new Employee("fake", 101, "N/A");
//		errorTemplate.send("avro-01-topic", employee);
//		
//		return "jai radhe krisha says error";
//	}
}
