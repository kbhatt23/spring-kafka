package com.learning.processors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learning.messages.nonrelatable.NonRelatableEntity;
import com.learning.util.StreamTransformUtil;
//@Configuration
public class JacksonJSONProcessor {

	private static final String INPUT_TOPIC = "jsontopic";
	private static final String OUTPUT_TOPIC = "jsontopic-stream";
	
	@Autowired
	private StreamTransformUtil streamTrasnformUtil;

	@Bean
	KStream<String, String> jacksonStreams(StreamsBuilder builder){
		KStream<String, String> inputStream = builder.stream(INPUT_TOPIC , Consumed.with(Serdes.String(), Serdes.String()));
		
		KStream<String,String> transformedStream= inputStream.mapValues(valueStrJson -> {
			NonRelatableEntity obj = streamTrasnformUtil.readValueFromString(valueStrJson, NonRelatableEntity.class);
			obj.setName(obj.getName().toUpperCase());//simpel use case just caps up the name
			return obj;
		})
				.mapValues(streamTrasnformUtil ::writeToJsonString)
				;
		
		//lets first convert the value again to string back as we are not suing json serializers
		transformedStream.to(OUTPUT_TOPIC);
		
		//print logs, do not use this in production this way
		inputStream.print(Printed.<String, String>toSysOut().withLabel("jacksonStreams: Original Stream"));
		transformedStream.print(Printed.<String, String>toSysOut().withLabel("jacksonStreams: Uppercase Stream"));
				
		return transformedStream;
	}
	
}
