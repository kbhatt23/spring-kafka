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
public class BulkAndNonBulkOrderProcessor {
	private static final String INPUT_TOPIC = "order-topic";
	private static final String OUTPUT_BULK_TOPIC = "order-bulk-topic";
	private static final String OUTPUT_NON_BULK_TOPIC = "order-transformed-topic";
	private static final String OUTPUT_SECURITY_TOPIC = "order-security-topic";
	
	
	@Autowired
	private StreamTransformUtil util;

	@Bean
	public KStream<String, OrderMessage> bulkOrderProcessing(StreamsBuilder builder){
		
		Serde<String> stringSerde= Serdes.String();
		JsonSerde<OrderMessage> jsonSerde = new JsonSerde<>(OrderMessage.class);
		JsonSerde<OrderMessageBulkQuantity> jsonSerdeBulk = new JsonSerde<>(OrderMessageBulkQuantity.class);
		
		KStream<String, OrderMessage> inputStream = builder.stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonSerde));
		
		
		inputStream.print(Printed.<String, OrderMessage>toSysOut().withLabel("bulkOrderProcessing: Original Stream"));
		
		// we do not want bulk orders to go to normal order topic
		//for normal order we want data to go to only non bulk topic and not bulk one
		//however masking is needed for both

		KStream<String, OrderMessage> maskedCardStream = inputStream.mapValues(order -> {
			String cardNumber = order.getCardNumber();
			String maskCard = util.maskCard(cardNumber);
			order.setCardNumber(maskCard);
			return order;
		});
		
		//for each order if item is fraud we must call a third party web service to push the data
		//consumer termination should do good
		maskedCardStream.filter((key,order) -> order.getCardNumber().toUpperCase().startsWith("FRAUD"))
					//card number contains fraud
		//.foreach(this :: callSecurityServiceForFraud);
		//in case we want to call web service and still make it intermediate oepration to still do kstream further processing
			.peek(this :: callSecurityServiceForFraud)
			.to(OUTPUT_SECURITY_TOPIC, Produced.with(stringSerde , jsonSerde));
			;
		
		
		//foreach is terminal oepration so we can not do other things with it
		//peek is same as for each in we want to call service and still keep on having kstream processing further

		KStream<String, OrderMessage>[] bulkNonBulkGrouped = maskedCardStream.branch( (key,order) -> util.isOrderBulk(order) , (k,v) -> true);
		
		KStream<String, OrderMessage> bulkOrderStream = bulkNonBulkGrouped[0];
		KStream<String, OrderMessage> nonBulkOrderStream = bulkNonBulkGrouped[1];
		
		KStream<String, OrderMessageBulkQuantity> transformedBulkOrder = bulkOrderStream.mapValues(util :: bulkTransform);
		transformedBulkOrder.to(OUTPUT_BULK_TOPIC, Produced.with(stringSerde, jsonSerdeBulk));
		
		
				
		//for non bulk pass toa nother topic
		
		nonBulkOrderStream.to(OUTPUT_NON_BULK_TOPIC,Produced.with(stringSerde, jsonSerde) );
		return maskedCardStream;
	}
	
	public void callSecurityServiceForFraud(String key, OrderMessage order) {
		System.out.println("callSecurityServiceForFraud: Calling securitu service for fraud order "+order);
	}
}
