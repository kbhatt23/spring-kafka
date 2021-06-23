package com.learning.processors;

import java.time.Duration;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.OnlineOrderMessage;
import com.learning.messages.OnlineOrderPaymentMessage;
import com.learning.messages.OnlinePaymentMessage;
import com.learning.util.OnlineOrderTimestampExtractor;
import com.learning.util.OnlinePaymentTimestampExtractor;

//@Configuration
public class OnlineOrderPaymentJoinProcess {

	
	@Bean
	public KStream<String, OnlineOrderMessage> manageOrderPaymentJoin(StreamsBuilder builder){
		Serde<String> stringSerde = Serdes.String();
		JsonSerde<OnlineOrderMessage> orderSerde = new JsonSerde<>(OnlineOrderMessage.class);
		JsonSerde<OnlinePaymentMessage> paymentSerde = new JsonSerde<>(OnlinePaymentMessage.class);
		JsonSerde<OnlineOrderPaymentMessage> mergedSerde = new JsonSerde<>(OnlineOrderPaymentMessage.class);
		
		KStream<String, OnlineOrderMessage> orderStream = builder.stream("online-order", Consumed.with(stringSerde,orderSerde , new OnlineOrderTimestampExtractor(), null));
		
		orderStream.print(Printed.<String, OnlineOrderMessage>toSysOut().withLabel("orderStream: initial Stream"));
		
		orderStream.mapValues(order -> {
			order.setItemName(order.getItemName().toUpperCase());
			return order;
		}).to("order-payment-inner", Produced.with(stringSerde,orderSerde ));
		
//		KStream<String, OnlinePaymentMessage> paymentStream = builder.stream("online-payment", Consumed.with(stringSerde, paymentSerde, new OnlinePaymentTimestampExtractor(),null));
//		
//		orderStream.print(Printed.<String, OnlineOrderMessage>toSysOut().withLabel("orderStream: initial Stream"));
//		paymentStream.print(Printed.<String, OnlinePaymentMessage>toSysOut().withLabel("paymentStream: initial Stream"));
//		//partition size for each topic shuld be exactly same
//		//also we must have duration/window to consider as stream is infinite
//		//lets have order first
//		//it waits unitil another paymenbt with same key comes
//		//we consider only for 1 hour window
//		KStream<String, OnlineOrderPaymentMessage> joinedMessage = orderStream.join(paymentStream, this::mergeOrderAndPayment, JoinWindows.of(Duration.ofHours(1)), 
//				Joined.with(stringSerde, orderSerde, paymentSerde)
//				);
//		
//		//one stream waits for other stream until window of 1 hour and then merge common key data and publish
//		
//		joinedMessage
//		.through("order-payment-inner", Produced.with(stringSerde,mergedSerde ))
//		.print(Printed.<String, OnlineOrderPaymentMessage>toSysOut().withLabel("manageOrderPaymentJoin: merged Stream"));
//		;
		
		return orderStream;
	}

	private OnlineOrderPaymentMessage mergeOrderAndPayment(OnlineOrderMessage order,OnlinePaymentMessage payment ) {
		return new OnlineOrderPaymentMessage(order.getOrderId(), payment.getPrice(),
				payment.getCreationDate(), payment.getCardNumber(), order.getCreationDate(), order.getItemName(),
				order.getQuantity());
		
	}
}
