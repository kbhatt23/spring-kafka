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
import com.learning.messages.OrderMessage;
import com.learning.util.OnlineOrderTimestampExtractor;
import com.learning.util.OnlinePaymentTimestampExtractor;

//@Configuration
public class OrderPaymentOneStream {

	@Bean
	public KStream<String, OnlineOrderMessage> kstreamOrderPayment(StreamsBuilder builder) {
		System.out.println("loading order favourite bean");
		Serde<String> stringSerde = Serdes.String();
		JsonSerde<OnlineOrderMessage> orderSerde = new JsonSerde<>(OnlineOrderMessage.class);
		//JsonSerde<OnlinePaymentMessage> paymentSerde = new JsonSerde<>(OnlinePaymentMessage.class);
		//JsonSerde<OnlineOrderPaymentMessage> orderPaymentSerde = new JsonSerde<>(OnlineOrderPaymentMessage.class);

		KStream<String, OnlineOrderMessage> orderStream = builder.stream("online-order",
				Consumed.with(stringSerde, orderSerde, new OnlineOrderTimestampExtractor(), null));
//		KStream<String, OnlinePaymentMessage> paymentStream = builder.stream("online-payment",
//				Consumed.with(stringSerde, paymentSerde, new OnlinePaymentTimestampExtractor(), null));

		// join
//		orderStream
//				.join(paymentStream, this::mergeOrderAndPayment, JoinWindows.of(Duration.ofHours(1)),
//						Joined.with(stringSerde, orderSerde, paymentSerde))
//				.to("order-payment-inner", Produced.with(stringSerde, orderPaymentSerde));
		
		orderStream.mapValues(order -> {
			order.setItemName(order.getItemName().toUpperCase());
			return order;
		}).through("fake-order")
		.print(Printed.<String, OnlineOrderMessage>toSysOut().withLabel("kstreamOrderPayment: transformed Stream"));
		;

		return orderStream;
	}

	private OnlineOrderPaymentMessage mergeOrderAndPayment(OnlineOrderMessage order,OnlinePaymentMessage payment ) {
		return new OnlineOrderPaymentMessage(order.getOrderId(), payment.getPrice(),
				payment.getCreationDate(), payment.getCardNumber(), order.getCreationDate(), order.getItemName(),
				order.getQuantity());
		
	}

}
