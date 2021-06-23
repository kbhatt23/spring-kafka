package com.learning.processors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.streams.kstream.Printed;
//@Configuration
public class HelloWorldStreamProcessor {

	private static final String HELLOWORLD_TOPIC = "helloworld";
	private static final String HELLOWORLD_UPPERCASE_TOPIC = "helloworld_uppercase";

	@Bean
	KStream<String, String> helloWorldStreamConfig(StreamsBuilder builder){
		
		KStream<String, String> inputStream =	builder.stream(HELLOWORLD_TOPIC, Consumed.with(Serdes.String(),Serdes.String())) // key adn value as string
					;
		
		//trasnsformation process
		KStream<String, String> transformedStream = inputStream.mapValues(s -> s.toUpperCase());
		
		transformedStream.to(HELLOWORLD_UPPERCASE_TOPIC);
		
		//print logs, do not use this in production this way
		inputStream.print(Printed.<String, String>toSysOut().withLabel("helloWorldStreamConfig: Original Stream"));
		transformedStream.print(Printed.<String, String>toSysOut().withLabel("helloWorldStreamConfig: Uppercase Stream"));
				
		return transformedStream;
	}
}
