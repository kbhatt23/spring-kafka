package com.learning.kafka_avro.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.learning.kafka_avro.beans.CustomerV2;
import com.learning.kafka_avro.consumers.KafkaConfig;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class CustomerV2AvroProducer {

	private static final String BASIC_AVRO_TOPIC = "customer-avro-topic";
	private static KafkaProducer<String, CustomerV2> producer;

	public static void main(String[] args) {

		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVER);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.APPLICATION_ID);
		properties.put("schema.registry.url", KafkaConfig.SCHEMA_REGISTRY_URL);

		// extra config as we want very high duplication as bank data should not be lost
		properties.put(ProducerConfig.ACKS_CONFIG, "all"); // when all min insync replicas are in sync then only
															// producer acknolwedge and message is committed

		properties.put(ProducerConfig.RETRIES_CONFIG, "3");

		producer = new KafkaProducer<>(properties);

		CustomerV2 customer = CustomerV2.newBuilder().setFirstName("krishna").setLastName("kanhayya").setAge(101)
				.setAutomatedEmail(true).setHeight(199.99f).setWeight(999.99f).build();
		ProducerRecord<String, CustomerV2> producerRecord = new ProducerRecord<>(BASIC_AVRO_TOPIC, customer);

		producer.send(producerRecord, 
				(RecordMetadata metadata, Exception exception) -> {
					if(exception != null) {
						System.out.println("CustomerV1AvroProducer: producer failed: "+exception);
					}else {
						System.out.println("CustomerV1AvroProducer: producer success");
					}
				}
				);

		// cleanup
		producer.flush();
		producer.close();
	
	}

}
