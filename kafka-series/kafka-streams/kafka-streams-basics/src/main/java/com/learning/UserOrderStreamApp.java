package com.learning;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.entities.Order;
import com.learning.entities.User;
import com.learning.entities.UserOrderData;

public class UserOrderStreamApp {
	private static final String USER_INPUT_TOPIC = "user-data";
	private static final String ORDER_INPUT_TOPIC = "order-data";
	private static final String OUTPUT_TOPIC = "user-order-data";
	private static final String APPLICATION_NAME = "user-order-app";
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static void main(String[] args) {

		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		

		StreamsBuilder streamsBuilder = new StreamsBuilder();
		GlobalKTable<String, String> userInputGlobalKTable = streamsBuilder.globalTable(USER_INPUT_TOPIC);
		KStream<String, String> orderInputStream = streamsBuilder.stream(ORDER_INPUT_TOPIC);

		KStream<String, String> userOrderDataStream = innerJoin(userInputGlobalKTable, orderInputStream);
		
		//KStream<String, String> userOrderDataStream = leftJoin(userInputGlobalKTable, orderInputStream);
		
		userOrderDataStream.to(OUTPUT_TOPIC);

		
		KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);
		
		kafkaStreams.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams :: close));

	}

	private static KStream<String, String> innerJoin(GlobalKTable<String, String> userInputGlobalKTable,
			KStream<String, String> orderInputStream) {
		KStream<String, String> userOrderDataStream = orderInputStream.join(userInputGlobalKTable, (k,v) -> k, 
					(orderDataStr, userDataStr) -> {
						System.out.println("UserOrderStreamApp: joining user data "+userDataStr);
						System.out.println("UserOrderStreamApp: joining order data "+orderDataStr);
						
						try {
							User userData = OBJECT_MAPPER.readValue(userDataStr, User.class);
							Order orderData = OBJECT_MAPPER.readValue(orderDataStr, Order.class);
							
							UserOrderData userOrderData = new UserOrderData(userData.getUserId(), orderData.getOrderId(), orderData.getProducts(), 
									orderData.getPrice()
									, userData.getName(), userData.getEmail());
							System.out.println("UserOrderStreamApp: final userdata object: "+userOrderData);
							return userOrderData;
						} catch (IOException e) {
							e.printStackTrace();
							return null;
						}
						
					}
				)
		.mapValues(userOrderDataObj -> {
			try {
				String userOrderDataStr =  OBJECT_MAPPER.writeValueAsString(userOrderDataObj);
				System.out.println("UserOrderStreamApp: final userdata string: "+userOrderDataStr);
				return userOrderDataStr;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return "";
			}
		})
		;
		return userOrderDataStream;
	}
	
	private static KStream<String, String> leftJoin(GlobalKTable<String, String> userInputGlobalKTable,
			KStream<String, String> orderInputStream) {
		KStream<String, String> userOrderDataStream = orderInputStream.leftJoin(userInputGlobalKTable, (k,v) -> k, 
					(orderDataStr, userDataStr) -> {
						System.out.println("UserOrderStreamApp: joining user data "+userDataStr);
						System.out.println("UserOrderStreamApp: joining order data "+orderDataStr);
						
						try {
							User userData = null;
							if(userDataStr != null)
								 userData = OBJECT_MAPPER.readValue(userDataStr, User.class);
							Order orderData = OBJECT_MAPPER.readValue(orderDataStr, Order.class);
							
							UserOrderData userOrderData = null;
							if(userData != null)
							 userOrderData = new UserOrderData(userData.getUserId(), orderData.getOrderId(), orderData.getProducts(), 
									orderData.getPrice()
									, userData.getName(), userData.getEmail());
							else
								userOrderData = new UserOrderData(null, orderData.getOrderId(), orderData.getProducts(), 
										orderData.getPrice()
										, null, null);
							System.out.println("UserOrderStreamApp: final userdata object: "+userOrderData);
							return userOrderData;
						} catch (IOException e) {
							e.printStackTrace();
							return null;
						}
						
					}
				)
		.mapValues(userOrderDataObj -> {
			try {
				String userOrderDataStr =  OBJECT_MAPPER.writeValueAsString(userOrderDataObj);
				System.out.println("UserOrderStreamApp: final userdata string: "+userOrderDataStr);
				return userOrderDataStr;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return "";
			}
		})
		;
		return userOrderDataStream;
	}


}
