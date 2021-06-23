package com.learning.processors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.FakeMessage;

@Configuration
public class FakeMessageProcess {

	@Bean
	public KStream<String, FakeMessage> build(StreamsBuilder builder){
		
		JsonSerde<FakeMessage> jsonSerde = new JsonSerde<>(FakeMessage.class);
		KStream<String, FakeMessage> peek = builder.stream("fake-test",Consumed.with(Serdes.String(), jsonSerde))
			.peek((key,value) -> System.out.println("jai shree ram input data : "+value+" with key "+key));
			
		;
		peek.to("delete-topic");
		return peek;
	}
}
