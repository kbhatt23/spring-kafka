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
public class OrderPayentProcessor {

	//@Bean
	public KStream<String, OnlineOrderPaymentMessage> handleOrderAndPayment(StreamsBuilder builder){
		Serde<String> stringSerde = Serdes.String();
		JsonSerde<OnlineOrderMessage> orderJsonSerde = new JsonSerde<>(OnlineOrderMessage.class);
		JsonSerde<OnlinePaymentMessage> paymentJsonSerde = new JsonSerde<>(OnlinePaymentMessage.class);
		JsonSerde<OnlineOrderPaymentMessage> mregedJsonSerde = new JsonSerde<>(OnlineOrderPaymentMessage.class);
		
		KStream<String, OnlineOrderMessage> orderStream = builder.stream("online-order",Consumed.with(stringSerde, orderJsonSerde, new OnlineOrderTimestampExtractor(),null));
		
		KStream<String, OnlinePaymentMessage> paymentStream = builder.stream("online-payment",Consumed.with(stringSerde, paymentJsonSerde, new OnlinePaymentTimestampExtractor(),null));

		//join automatically check for common keys 
		//so other stream waits if one arrives only until the window runs out
		KStream<String, OnlineOrderPaymentMessage> mergedStream=	orderStream.join(paymentStream, this::mergeOrderAndPayment, JoinWindows.of(Duration.ofSeconds(10)), 
				Joined.with(stringSerde, orderJsonSerde, paymentJsonSerde)
				);
		
		mergedStream.through("order-payment-inner",Produced.with(stringSerde, mregedJsonSerde))
			.print(Printed.<String, OnlineOrderPaymentMessage>toSysOut().withLabel("handleOrderAndPayment: Final Stream"));
		;
		
		return mergedStream;
	}

	private OnlineOrderPaymentMessage mergeOrderAndPayment(OnlineOrderMessage order,OnlinePaymentMessage payment ) {
		return new OnlineOrderPaymentMessage(order.getOrderId(), payment.getPrice(),
				payment.getCreationDate(), payment.getCardNumber(), order.getCreationDate(), order.getItemName(),
				order.getQuantity());
		
	}
}
