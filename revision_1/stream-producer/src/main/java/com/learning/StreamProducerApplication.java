package com.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.model.Employee;
import com.learning.model.Order;

@SpringBootApplication
//create a producer outbound channel with name output also creates exchnage with same name, OOB
//since it is producer it do not create queue at all
@EnableBinding({Source.class, CustomProducerSource.class,OrderProducerSource.class})
//creater a channel with name output and exchange with same name
//wont create queue as it is not listener
//only stream listner binder can create both exchange and queue

@RestController
@RequestMapping("/publish")
public class StreamProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamProducerApplication.class, args);
	}

	//source have output channel already created
	@Autowired
	private Source source;
	
	@Autowired
	private CustomProducerSource customSource;
	
	@Autowired
	private OrderProducerSource orderProducerSource;
	
	//lets create custom source
	
	@PostMapping
	public String publish(@RequestBody Employee employee) {
		source.output().send(MessageBuilder.withPayload(employee).build());
		return "jai shree ram";
	}
	
	@PostMapping("/custom")
	public String publishCustom(@RequestBody Employee employee) {
		customSource.output().send(MessageBuilder.withPayload(employee).build());
		return "jai shree ram from custom";
	}
	
	@PostMapping("/orders")
	public String publishCustom(@RequestBody Order order) {
		orderProducerSource.output().send(MessageBuilder.withPayload(order).build());
		return "jai shree ram from orders";
	}
}
