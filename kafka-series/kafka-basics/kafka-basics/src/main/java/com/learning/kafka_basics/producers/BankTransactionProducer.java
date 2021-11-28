package com.learning.kafka_basics.producers;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka_basics.config.KafkaConfig;
import com.learning.kafka_basics.entities.BankTransaction;
import com.learning.kafka_basics.entities.TransactionType;

public class BankTransactionProducer {

	private static final String BANK_TRANSACTIONS_TOPIC = "bank-transactions-topic";

	private static final List<String> USERS = Arrays.asList("kbhatt23", "kanhayya108", "narayan23", "mahesh101");

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final Random RANDOM = new Random();
	private static final int TRANSACTIONS_COUNT = 11;
	private static KafkaProducer<String, String> producer ;

	private static String getUser() {
		return USERS.get(RANDOM.nextInt(4));
	}

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVER);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.APPLICATION_ID);
		
		//extra config as we want very high duplication as bank data should not be lost
		properties.put(ProducerConfig.ACKS_CONFIG, "all"); //when all min insync replicas are in sync then only producer acknolwedge and message is committed
		
		properties.put(ProducerConfig.RETRIES_CONFIG, "3");
		properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
		
		//but works only for kafka streams : kafka-.kafka communication
		properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		

		producer = new KafkaProducer<>(properties);
		IntStream.rangeClosed(1, TRANSACTIONS_COUNT).forEach(BankTransactionProducer::sendBankTransactionToKafka);

		//cleanup
		producer.flush();
		producer.close();
	}

	private static void sendBankTransactionToKafka(int index) {
		try {
			BankTransaction bankTransaction = new BankTransaction(getUser(), getRandomAmount(index), 
					getTransactionType()
					);
			System.out.println("BankTransactionProducer: Transmitting transaction: "+bankTransaction);
			String writeValueAsString = OBJECT_MAPPER.writeValueAsString(bankTransaction);
			// round robin among partitions
						ProducerRecord<String, String> record = new ProducerRecord<String, String>(BANK_TRANSACTIONS_TOPIC,
								writeValueAsString);
						producer.send(record,
								//callback once internal queue buffer is sucesfully pusehd to kafka topic based on acks rule
								(RecordMetadata metadata, Exception exception ) -> {
										if(exception != null) {
											System.out.println("BankTransactionProducer: error occurred while producing message: "+exception.getMessage());
										}
										else {
//											System.out.println("BankTransactionProducer: message published to topic: "+metadata.topic()+" at partition: "+metadata.partition()
//											+" at offset: "+metadata.offset()
//													);
											
										}
									}
								);
					
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private static TransactionType getTransactionType() {
		return RANDOM.nextBoolean() ? TransactionType.CREDIT: TransactionType.DEBIT;
	}

	private static double getRandomAmount(int index) {
		return (index * 100 * RANDOM.nextDouble())+100;
	}
}
