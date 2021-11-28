package com.learning;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;

//assume key is authorname and value is the total pages in the new book launched
//on every new book launch we send message to the topic
//calculate total pages of all the books launched per author
//need not ktable as there is no upsert but always a new book should be inserted

public class FindPageLengthByWriterStarterApp {
	private static final String SPLITTER = ",";
	private static final String INPUT_TOPIC = "book-launch-topic";
	private static final String OUTPUT_TOPIC = "author-total-pages-topic";
	private static final String APPLICATION_NAME = "page-length-app";

	public static void main(String[] args) {

		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		
		StreamsBuilder streamsBuilder = new StreamsBuilder();
		//as of now key is null and value is authorname,new book page count
		KStream<String, String> inputStream = streamsBuilder.stream(INPUT_TOPIC);
		
		KStream<String, Long> interMediateStream = inputStream.peek((key,value) -> System.out.println("FindPageLengthByWriterStarterApp: new book data recieved with key:"+key+", value:"+value))
				   .filter((key,value) -> value.contains(SPLITTER)) // the value is combination of author,pagecount
				   .filter((key,value) -> isLong(value.split(SPLITTER)[1])) // validate if second item is integer
				   .map((key,value) -> KeyValue.pair(value.split(SPLITTER)[0], Long.valueOf(value.split(SPLITTER)[1])))
				   ;
		
		
		KTable<String, Long> finalTable =  interMediateStream
				.peek((k,v) -> System.out.println("FindPageLengthByWriterStarterApp: intermdaite topic key:"+k+"and value:"+v))
				.groupByKey(Serialized.with(Serdes.String(), Serdes.Long()))
					      .aggregate(() -> 0L, (key,value,storage) -> storage + value,
					    		  Materialized.with(Serdes.String(), Serdes.Long())
					    		  )
					    //reduce is same as aggregation bubt the inout and output type is same
					      //in aggregation we can have different data type of output van and stream val
				//.count(Materialized.with(Serdes.String(), Serdes.Long()))
		;
		
		finalTable.toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
		
		KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);
		kafkaStreams.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams :: close));
	
	}
	
	public static boolean isLong(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        long d = Long.parseLong(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}
