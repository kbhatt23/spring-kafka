package com.learning.processors;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.OrderMessage;

//called for all orders and card number is masked
@Configuration
public class CreditCardMAskProcessor {

	private static final String INPUT_TOPIC = "order-topic";
	private static final String OUTPUT_TOPIC = "order-masked-topic";
	
	@Bean
	public KStream<String, OrderMessage> maskOrderCreditCard(StreamsBuilder builder){
		JsonSerde<OrderMessage> jsonSerde = new JsonSerde<>(OrderMessage.class);
		Serde<String> stringSerde = Serdes.String();
		KStream<String, OrderMessage> inputStream = builder.stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonSerde));
		inputStream.print(Printed.<String, OrderMessage>toSysOut().withLabel("maskOrderCreditCard: Original Stream"));

		KStream<String, OrderMessage> outputStream = inputStream.mapValues(order -> {
			String cardNumber = order.getCardNumber();
			int maskLength =4;
			StringBuilder maskedChars = new StringBuilder();
			for(int i =0 ; i < 4; i++) {
				maskedChars.append("*");
			}
			//mask all excpet last 4 chars
			cardNumber = cardNumber.substring(0, cardNumber.length()-maskLength) + maskedChars.toString();
			order.setCardNumber(cardNumber);
			return order;
		});
		outputStream.print(Printed.<String, OrderMessage>toSysOut().withLabel("maskOrderCreditCard: Output Stream"));
	
		outputStream.to(OUTPUT_TOPIC , Produced.with(stringSerde, jsonSerde));
		return outputStream;
	}
}
