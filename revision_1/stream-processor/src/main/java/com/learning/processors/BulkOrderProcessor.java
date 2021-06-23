package com.learning.processors;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.OrderMessage;
import com.learning.messages.OrderMessageBulkQuantity;
import com.learning.util.StreamTransformUtil;

//@Configuration
public class BulkOrderProcessor {
	private static final String INPUT_TOPIC = "order-topic";
	private static final String OUTPUT_BULK_TOPIC = "order-bulk-topic";
	private static final String OUTPUT_NON_BULK_TOPIC = "order-transformed-topic";
	
	@Autowired
	private StreamTransformUtil util;

	@Bean
	public KStream<String, OrderMessage> bulkOrderProcessing(StreamsBuilder builder){
		
		Serde<String> stringSerde= Serdes.String();
		JsonSerde<OrderMessage> jsonSerde = new JsonSerde<>(OrderMessage.class);
		JsonSerde<OrderMessageBulkQuantity> jsonSerdeBulk = new JsonSerde<>(OrderMessageBulkQuantity.class);
		
		KStream<String, OrderMessage> inputStream = builder.stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonSerde));
		
		inputStream.print(Printed.<String, OrderMessage>toSysOut().withLabel("bulkOrderProcessing: Original Stream"));

		KStream<String, OrderMessage> maskedCardStream = inputStream.mapValues(order -> {
			String cardNumber = order.getCardNumber();
			String maskCard = util.maskCard(cardNumber);
			order.setCardNumber(maskCard);
			return order;
		});
		

		//only for bulk extra we push to another topic
		KStream<String, OrderMessageBulkQuantity> bulkStream = maskedCardStream.filter((key,order ) -> util.isOrderBulk(order))
					  .mapValues(util :: bulkTransform )
		;
		
		bulkStream.to(OUTPUT_BULK_TOPIC, Produced.with(stringSerde, jsonSerdeBulk));
				
		//for all orders we pass to main topic 
		
		maskedCardStream.to(OUTPUT_NON_BULK_TOPIC,Produced.with(stringSerde, jsonSerde) );
		return maskedCardStream;
	}
}
