package com.learning.kafka_basics.consumers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.learning.kafka_basics.config.KafkaConfig;

public class SimpleStringConsumer {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVER);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_DESERIALIZER);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_DESERIALIZER);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "simple-string-group");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
		
		consumer.subscribe(Arrays.asList("simple-string"));
		
		while(true) {
			//poll every 100 ms
		ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100))	;
		for(ConsumerRecord<String, String> record: records) {
			System.out.println("SimpleStringConsumer: recieved record with key: "+record.key()+
					"with value: "+record.value()+" from partition "+record.partition()+" of topic "+record.topic()
					);
			
		}
		}
		
	}
}
