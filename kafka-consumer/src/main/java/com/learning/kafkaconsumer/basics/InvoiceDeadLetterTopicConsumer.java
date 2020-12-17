package com.learning.kafkaconsumer.basics;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafkaconsumer.basics.entity.Invoice;

//@Service
public class InvoiceDeadLetterTopicConsumer {

	Logger logger = LoggerFactory.getLogger(InvoiceDeadLetterTopicConsumer.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	//since we have 2 partitions divided based on key , lets have 2 seperate thread for processing
	//in container factory -> we provide error handler with retyable template and then push to dead letter topic
	@KafkaListener(topics = "invoice-topic",containerFactory = "deadLetterContainerFactory" , concurrency = "2")
	public void recieveInvoice(String invoiceStr) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		Invoice invoice = mapper.readValue(invoiceStr, Invoice.class);
		logger.info("Starting processing Invoice "+invoice);
		if(invoice.getPrice() < 0) {
			//price can nt be negative
			throw new RuntimeException("Invoice can not have negative price: "+invoice);
		}
		//simulating processing time
		Thread.sleep(200);
		logger.info("Succesfully processed Invoice "+invoice);
	}
}
