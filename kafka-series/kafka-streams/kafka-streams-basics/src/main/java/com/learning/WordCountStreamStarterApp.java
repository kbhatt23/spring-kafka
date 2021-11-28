package com.learning;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

public class WordCountStreamStarterApp {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-multi-app");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	 	properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	 	
	 	StreamsBuilder builder = new StreamsBuilder();
	 	
	 	KStream<String, String> inputStream = builder.stream("sentence-multi-topic");

	 	KTable<String, Long> table = inputStream.peek((key,value) -> System.out.println("WordCountStreamStarterApp: recieving input with key: "+key+" and value: "+value))
	 			  //ignorecase
	 			  .mapValues(word -> word.toLowerCase())
	 			  .flatMapValues(word -> Arrays.asList(word.split(" ")))
	 			  .selectKey((key,value) -> value) // key updated from null things to the word itseld both key and value
	 			  .groupByKey()
	 			  .count()
	 			  ;
	 	
	 	table.toStream()
	 			.to("word-count-multi-topic", Produced.with(Serdes.String(), Serdes.Long()));
	 	
	 	
	 	Topology topology = builder.build();
	 	
	 	KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
	 	
	 	kafkaStreams.start();
	 	
//	 	try {
//			Thread.sleep(1000000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	 	
//	 	kafkaStreams.close();
	 	
	 	//making the server blocked and waiting
	 	//main thread wont die until forcefully clicked on red button
	 	Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams :: close));
	}
}
