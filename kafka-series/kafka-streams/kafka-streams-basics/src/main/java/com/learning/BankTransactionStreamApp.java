package com.learning;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.entities.BankTransaction;
import com.learning.entities.TransactionType;
import com.learning.serdes.AppSerdes;

public class BankTransactionStreamApp {
	private static final String OUTPUT_TOPIC = "user-balance-topic";
	private static final String INPUT_TOPIC = "bank-transactions-topic";
	private static final String APPLICATION_NAME = "bank-transactions-app";
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static void main(String[] args) {

		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		
		//exactly once gurantee
		properties.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

		// bank transaction should be exactly processed once
		// properties.put(StreamsConfig.EXACTLY_ONCE, properties)

		StreamsBuilder streamsBuilder = new StreamsBuilder();
		KStream<String, String> inputStream = streamsBuilder.stream(INPUT_TOPIC);


		KGroupedStream<String, BankTransaction> grouped = inputStream
				.peek((key, value) -> System.out.println("BankTransactionStreamApp: input recieved " + value))
				.mapValues((k, v) -> readBankTransaction(v)).filter(BankTransactionStreamApp::isValidBankTransaction)
				.selectKey((k, v) -> v.getUserName()) // marked for repartition
				.groupByKey(Grouped.with(APPLICATION_NAME, Serdes.String(), AppSerdes.bankTransaction()));
		;

		KTable<String, Double> aggregate = grouped.aggregate(() -> 0d,
				(userName, bankTransaction,
						currentBalance) -> bankTransaction.getTransactionType() == TransactionType.CREDIT
								? currentBalance + bankTransaction.getAmount()
								: currentBalance - bankTransaction.getAmount(),
				Materialized.with(Serdes.String(), Serdes.Double()))

		;

		aggregate.toStream()
				.peek((userName, balance) -> System.out
						.println("BankTransactionStreamApp: final balance is: " + balance + " of user: " + userName))
				.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Double()));
		
		KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);
		
		kafkaStreams.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams :: close));

	}

	private static BankTransaction readBankTransaction(String str) {
		try {
			// reading code
			return OBJECT_MAPPER.readValue(str.getBytes(), BankTransaction.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static boolean isValidBankTransaction(String key, BankTransaction bankTransaction) {
		return bankTransaction != null && bankTransaction.getUserName() != null
				&& !bankTransaction.getUserName().isEmpty() && bankTransaction.getAmount() > 0d
				&& (TransactionType.CREDIT == bankTransaction.getTransactionType()
						|| TransactionType.DEBIT == bankTransaction.getTransactionType());
	}

}
