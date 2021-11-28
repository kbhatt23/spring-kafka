package com.learning;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

public class FavoriteColorIndirectTableStarterApp {

	private static final String FAVORITE_COLORS_OUTPUT_TOPIC = "favorite-colors";
	private static final String USER_COLORS_INTERMEDIATE_TOPIC = "user-colors-table";
	private static final String USER_COLOR_INPUT_TOPIC = "user-colors";
	private static final String APPLICATION_NAME = "favorite-color-app";
	private static final List<String> VALID_COLORS = Arrays.asList("red","yellow","orange","white","black");

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		
		StreamsBuilder streamsBuilder = new StreamsBuilder();
		KStream<String, String> inputStream = streamsBuilder.stream(USER_COLOR_INPUT_TOPIC);
		
		inputStream.peek((key,value) -> System.out.println("FavoriteColorIndirectTableStarterApp: input recieved with key:"+key+", value:"+value))
				   .filter((key,value) ->  value != null && !value.isEmpty())
				   .filter((key,value) -> VALID_COLORS.contains(value.split(",")[1]))
				   .peek((k,v) -> System.out.println("FavoriteColorIndirectTableStarterApp: filtered input with value: "+v))
				   //since we need to update key and value oth lets use map method -> will be marked to repartition
			       .map((key,value) -> KeyValue.pair(value.split(",")[0], value.split(",")[1]))
			       .to(USER_COLORS_INTERMEDIATE_TOPIC);
		
		KTable<String, String> table = streamsBuilder.table(USER_COLORS_INTERMEDIATE_TOPIC);
		table
		  .groupBy((k,v) -> KeyValue.pair(v, v))
		  .count()
		   .toStream()
		   .to(FAVORITE_COLORS_OUTPUT_TOPIC,
				   Produced.with(Serdes.String(), Serdes.Long())
				   );
		  
		;		
		
		KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);
		kafkaStreams.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams :: close));
	}
}
