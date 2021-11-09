package com.learning.kafka_basics.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.learning.kafka_basics.config.KafkaConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleStringProducerCallback {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVER);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.APPLICATION_ID);

		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

		for (int i = 0; i < 10; i++) {
			// round robin among partitions
			ProducerRecord<String, String> record = new ProducerRecord<String, String>("simple-string",
					"SimpleStringProducerCallback. jai shree ram - " + i);
			producer.send(record,
					//callback once internal queue buffer is sucesfully pusehd to kafka topic based on acks rule
					(RecordMetadata metadata, Exception exception ) -> {
							if(exception != null) {
								System.out.println("SimpleStringProducerCallback: error occurred while producing message: "+exception.getMessage());
							}
							else {
								System.out.println("SimpleStringProducerCallback: message published to topic: "+metadata.topic()+" at partition: "+metadata.partition()
								+" at offset: "+metadata.offset()
										);
								
							}
						}
					);
		}
		
		
		producer.flush();
		producer.close();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
