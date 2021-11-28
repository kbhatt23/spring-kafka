package com.learning;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

/**
 * Hello world!
 *
 */
public class SentenceLengthStreamStarterApp 
{
    public static void main( String[] args )
    {
    	Properties properties = new Properties();
    	
    	//consumer group id and prefix of internal changelog topic for fault tolerance
    	properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app");
    	
    	properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    	properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    	
    	properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    	properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    	
    	StreamsBuilder builder = new StreamsBuilder();
    	
    	//to sentence-length-topic
    	
    	KStream<String, String> inputStream = builder.stream("sentence-topic");
    	
    	inputStream.peek((key,value) -> System.out.println("SentenceLengthStreamStarterApp: input sentence with key: "+key+" and value: "+value))
    			.mapValues(String :: length)
    			.to("sentence-length-topic", Produced.with(Serdes.String(), Serdes.Integer()));
    			
    	Topology topology = builder.build();
    	KafkaStreams stream = new KafkaStreams(topology, properties);
    	
    	stream.start();
    	
    	try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	stream.close();
    	
    	
    }
}
