package com.learning.processors;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.FeedBackStoreValue;
import com.learning.messages.ItemFeedBack;
import com.learning.messages.ItemFeedBackResult;
import com.learning.transformers.FeedBackAverageTransformer;

@Configuration
public class ItemFeedbackProcessor {

	@Bean
	KStream<String, ItemFeedBack> processFeedBack(StreamsBuilder builder){
		//use table with key as item name and value as rating as rating changes for same item name
		Serde<String> stringSerde = Serdes.String();
		JsonSerde<ItemFeedBack> jsonSerde = new JsonSerde<>(ItemFeedBack.class);
		JsonSerde<ItemFeedBackResult> feedbackRatingOneSerde = new JsonSerde<>(ItemFeedBackResult.class);
		JsonSerde<FeedBackStoreValue> storeSerde= new JsonSerde<>(FeedBackStoreValue.class);

		KStream<String, ItemFeedBack> inputStream = builder.stream("itemFeedBack", Consumed.with(stringSerde, jsonSerde));
		;
		String storeName = "feedbackRatingOneStateStore";
		
		//create store in place
		//if already exist it skips creating new
		KeyValueBytesStoreSupplier storeSupplier = Stores.inMemoryKeyValueStore(storeName);
		StoreBuilder<KeyValueStore<String, FeedBackStoreValue>> storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, stringSerde, storeSerde);

		builder.addStateStore(storeBuilder);
		

		inputStream
				.transformValues(() -> new FeedBackAverageTransformer(storeName),
						storeName)
				.to("itemFeedBack-result", Produced.with(stringSerde, feedbackRatingOneSerde));

		return inputStream;
		
		
		
	}
}
