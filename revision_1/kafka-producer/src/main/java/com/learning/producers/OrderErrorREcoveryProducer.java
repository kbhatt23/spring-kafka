package com.learning.producers;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.producers.entities.OrderEntity;
import com.learning.producers.entities.SimpleNumber;

//@Service
public class OrderErrorREcoveryProducer implements CommandLineRunner{

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void run(String... args) throws Exception {

		//send 10 orders 5 goos 5 bad
		//if price is negative there is no way we can even retry and succesfully consume message
		IntStream.rangeClosed(1, 10)
		.mapToObj(number -> number % 2 == 0 ? new OrderEntity("order-"+number, number*10d)
				: 
			new OrderEntity("order-"+number, -1d))
		//if number is even it is good order, otherwise have negatve amnt
		.forEach(order -> 
				{
					try {
						kafkaTemplate.send("ordertopic", objectMapper.writeValueAsString(order));
						//along with this also send simple message
						//in consumer kafkalisterne we will not add error handler so it will show default spring behaviour
						//default spring behaviour is no retry log big error stack and update offset of consumer to next element
						kafkaTemplate.send("simplenumbertopic", 
								objectMapper.writeValueAsString(new SimpleNumber(Integer.parseInt(order.getOrderID().split("-")[1])))
								);
						
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
				);
		;
	}

	
}
