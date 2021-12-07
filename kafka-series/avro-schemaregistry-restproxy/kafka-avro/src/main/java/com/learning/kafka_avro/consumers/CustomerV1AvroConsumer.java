package com.learning.kafka_avro.consumers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.learning.kafka_avro.beans.CustomerV1;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;

public class CustomerV1AvroConsumer {
	
	private static final String BASIC_AVRO_TOPIC = "customer-avro-topic";

	public static void main(String[] args) {

		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVER);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_DESERIALIZER);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-avro-customer-group");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		
		properties.put("schema.registry.url", KafkaConfig.SCHEMA_REGISTRY_URL);
		properties.put("specific.avro.reader", "true");
		
		KafkaConsumer<String, CustomerV1> consumer = new KafkaConsumer<>(properties);

		consumer.subscribe(Arrays.asList(BASIC_AVRO_TOPIC));

		while (true) {
			// poll every 100 ms
			ConsumerRecords<String, CustomerV1> records = consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, CustomerV1> record : records) {
				System.out.println("CustomerV1AvroConsumer: recieved record with key: " + record.key() + "with value: "
						+ record.value() + " from partition " + record.partition() + " of topic " + record.topic());

			}
		}

	}
}
