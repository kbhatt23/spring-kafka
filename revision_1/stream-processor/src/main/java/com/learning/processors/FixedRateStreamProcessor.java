package com.learning.processors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.streams.kstream.Printed;
//@Configuration
public class FixedRateStreamProcessor {

	private static final String INPUT_TOPIC = "fixedrate";
	private static final String OUTPUT_TOPIC = "fixedrateUpperCase";

	@Bean
	KStream<String, String> fixedRateStreamConfig(StreamsBuilder builder){
		
		KStream<String, String> inputStream =	builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(),Serdes.String())) // key adn value as string
					;
		
		//trasnsformation process
		KStream<String, String> transformedStream = inputStream.mapValues(s -> s.toUpperCase());
		
		transformedStream.to(OUTPUT_TOPIC);
		
		//print logs, do not use this in production this way
		inputStream.print(Printed.<String, String>toSysOut().withLabel("fixedRateStreamConfig: Original Stream"));
		transformedStream.print(Printed.<String, String>toSysOut().withLabel("fixedRateStreamConfig: Uppercase Stream"));
				
		return transformedStream;
	}
}
