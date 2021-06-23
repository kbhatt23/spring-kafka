package com.learning.processors;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.InventoryItem;
import com.learning.messages.ItemType;

//@Configuration
public class InventoryItemStreamProcessor {

	@Bean
	public KStream<String, InventoryItem> createPublish(StreamsBuilder streamsBuilder){
		
		Serde<String> stringSerde = Serdes.String();
		JsonSerde<InventoryItem> jsonSerdeInventoryItem = new JsonSerde<>(InventoryItem.class);
		KStream<String, InventoryItem> inputStream = streamsBuilder.stream("inventoryupdate",Consumed.with(stringSerde, jsonSerdeInventoryItem));

		//in case of sell we discount the item
		inputStream.mapValues(inventoryItem -> {
			if(inventoryItem.getItemType().equals(ItemType.SELL)) {
				return -1 * inventoryItem.getQuantity();
			}else {
				return inventoryItem.getQuantity();
			}
			})
			.groupByKey() // for same itemname we club together
			//lets assume we can not hanlde negative case
			.aggregate(() -> 0l, (key,value,aggregate) -> aggregate+value, 
					Materialized.with(stringSerde, Serdes.Long()))
			.toStream()
			.peek((key,value) -> System.out.println("final end result key: "+key+" and value: "+value))
			.to("inventoryupdate-live", Produced.with(stringSerde, Serdes.Long()));
			;
		;
		
		return inputStream;
	}
}
