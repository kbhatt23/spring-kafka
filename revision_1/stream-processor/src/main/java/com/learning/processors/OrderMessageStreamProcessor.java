package com.learning.processors;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.OrderMessage;

import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
//@Configuration
public class OrderMessageStreamProcessor {

	private static final String INPUT_TOPIC = "order-topic";
	private static final String OUTPUT_TOPIC = "order-transformed-topic";

	//let json serialize on its own
	@Bean
	KStream<String, OrderMessage> orderMessageStreamPRocess(StreamsBuilder builder){
		Serde<OrderMessage> valueSerde=  new JsonSerde<>(OrderMessage.class); 
		// auomatically converts string to object
		//however the package name of bean should be same in processor and producer and consumer
		
		KStream<String, OrderMessage> inputStream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String() ,valueSerde ));
	
		inputStream.print(Printed.<String, OrderMessage>toSysOut().withLabel("orderMessageStreamPRocess: Original Stream"));
		//transform
		KStream<String, OrderMessage> transformedStream = inputStream.mapValues(obj ->{ obj.setItemName(obj.getItemName().toUpperCase())
		;
		return obj;
		});
		
		//automatically conert object to string json
		//however the package name of bean shuld be same in processor and consumer
		transformedStream.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), valueSerde));
		
		
		transformedStream.print(Printed.<String, OrderMessage>toSysOut().withLabel("orderMessageStreamPRocess: Transformed Stream"));
		
		return transformedStream;
	}
}
