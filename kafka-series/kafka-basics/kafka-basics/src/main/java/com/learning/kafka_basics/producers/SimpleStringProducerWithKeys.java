package com.learning.kafka_basics.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.learning.kafka_basics.config.KafkaConfig;

//demo for multiple consumer group
//demo for routing to specific partitions based on key
public class SimpleStringProducerWithKeys {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVER);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		//properties.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.APPLICATION_ID);

		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

		for (int i = 0; i < 10; i++) {
			// odd should go to partition 1
			// even should go to partition 0
			String key  = i % 2 == 0 ? "id_" + KafkaConfig.EVEN_KEY:"id_" +KafkaConfig.ODD_KEY;
			ProducerRecord<String, String> record = new ProducerRecord<String, String>("simple-string-keys-multi",key,
					"SimpleStringProducerWithKeys. jai shree ram - " + i);
			
			producer.send(record,
					//callback once internal queue buffer is sucesfully pusehd to kafka topic based on acks rule
					(RecordMetadata metadata, Exception exception ) -> {
							if(exception != null) {
								System.out.println("SimpleStringProducerWithKeys: error occurred while producing message: "+exception.getMessage());
							}
							else {
								System.out.println("SimpleStringProducerWithKeys: message published to topic: "+metadata.topic()+" at partition: "+metadata.partition()
								+" at offset: "+metadata.offset()
										);
								
							}
						}
					);
		}
		
		
		producer.flush();
		producer.close();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
