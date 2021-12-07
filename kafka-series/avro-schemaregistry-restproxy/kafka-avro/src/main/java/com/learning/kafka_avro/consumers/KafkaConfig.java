package com.learning.kafka_avro.consumers;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaConfig {

	public static final String BOOTSTRAP_SERVER = "http://localhost:9092";
	public static final String APPLICATION_ID = "kafka-avro-application";
	public static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";
	public static final String STRING_SERIALIZER = StringSerializer.class.getName();
	public static final String STRING_DESERIALIZER = StringDeserializer.class.getName();

}
