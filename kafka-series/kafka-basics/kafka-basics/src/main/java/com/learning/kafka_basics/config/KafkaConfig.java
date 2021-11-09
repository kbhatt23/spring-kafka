package com.learning.kafka_basics.config;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaConfig {

	public static final String BOOTSTRAP_SERVER = "localhost:9092";
	public static final String STRING_SERIALIZER = StringSerializer.class.getName();
	public static final String INTEGER_SERIALIZER = IntegerSerializer.class.getName();
	public static final String APPLICATION_ID = "kafka-basic-application";
	public static final String APPLICATION_ID_MULTI = "kafka-basic-application-multi";
	
	public static final int ODD_KEY = 1; // should go to partition 1
	public static final int EVEN_KEY = 0; // should go to partition 0
	
	
	public static final String STRING_DESERIALIZER = StringDeserializer.class.getName();
}
