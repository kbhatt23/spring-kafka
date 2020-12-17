package com.learning.kafkaproducer.basics;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaproducer.basics.entity.Image;
import com.learning.kafkaproducer.basics.entity.Invoice;

//@Service
public class InvoiceDeadLetterTopicProducer implements CommandLineRunner{

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void run(String... args) throws Exception {
		
		IntStream.rangeClosed(1, 10)
				 .mapToObj(number -> {
					 if(number % 2 == 0) {
						 //even numbers we put error amount
						 return new Invoice("Invoice-"+number, -1);
					 }else {
						 return new Invoice("Invoice-"+number, ThreadLocalRandom.current().nextDouble(100, 1000));
					 }
				 })
				 .forEach(this::sendToKafka);
				 ;
		
	}
	
	public void sendToKafka(Invoice invoice) {
		try {
			kafkaTemplate.send("invoice-topic", invoice.getId(), 
					objectMapper.writeValueAsString(invoice)
					);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
