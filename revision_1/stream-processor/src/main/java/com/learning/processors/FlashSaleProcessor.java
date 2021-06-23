package com.learning.processors;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.FlashSale;

//@Configuration
public class FlashSaleProcessor {

	private static final String INPUT_TOPIC = "flashsale";
	private static final String INPUT_TABLE_TOPIC = "flashsale-table";
	
	private static final String OUTPUT_CUSTOMER_COUNT = "flashsale-customers";
	private static final String OUTPUT_VOTE_COUNT = "flashsale-votes";
	
	@Bean
	public KStream<String, String> manageFlashSale(StreamsBuilder builder){
		
		Serde<String> stringSerde = Serdes.String();
		JsonSerde<FlashSale> jsonSerde = new JsonSerde<>(FlashSale.class);
		//get data
		KStream<String, String> customerStream= builder.stream(INPUT_TOPIC,Consumed.with(stringSerde,jsonSerde))
				.map((key,flashSale) -> KeyValue.pair(flashSale.getCustomerName(), flashSale.getItemName()));
			
		customerStream//since customer name for each event remains same and itemname can change
		.to(INPUT_TABLE_TOPIC);
		
		KTable<String, String> table = builder.table(INPUT_TABLE_TOPIC, Consumed.with(stringSerde, stringSerde));
		
		//unique customers total count
		table.groupBy((k,v) -> KeyValue.pair(k, k))
			.count()
			.toStream()
			.mapValues(String::valueOf)
			.to(OUTPUT_CUSTOMER_COUNT);
		;// key will be customer and value will be customer
		
		//find count of items names unique
		//we can group by value and all items will be grouped like that
		//same value ones are clubbed together and count can be foud
		table.groupBy((key,value) -> KeyValue.pair(value,value)).count().toStream()
		.mapValues(String::valueOf)
		.to(OUTPUT_VOTE_COUNT);
		//key will be itemname and value will be count of each
		return customerStream;
	}
}
