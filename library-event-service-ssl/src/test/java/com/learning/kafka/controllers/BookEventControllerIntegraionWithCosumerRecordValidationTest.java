package com.learning.kafka.controllers;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.learning.kafka.messages.Book;
import com.learning.kafka.messages.LibraryEvent;

//if not set this will run the test on same port as that of the main application
//this ensures that port is always available for test
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@EmbeddedKafka(topics = {"library-events"}, partitions = 3)
//take values from test embeded kafka
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
"spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class BookEventControllerIntegraionWithCosumerRecordValidationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	//key value paired consumer
	private Consumer<String, LibraryEvent> consumer;
	
	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;
	
	BlockingQueue<ConsumerRecord<String, LibraryEvent>> records;

    KafkaMessageListenerContainer<String, LibraryEvent> container;
	
	//before ach test we must setup a fresh consumer representing kafkalistner
	@Before
	public void setUp() {
		 Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "true", embeddedKafkaBroker));
		 JsonDeserializer<LibraryEvent> valueDeserializer = new JsonDeserializer<>(LibraryEvent.class);
         valueDeserializer.addTrustedPackages("*");
	        DefaultKafkaConsumerFactory<String, LibraryEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(),valueDeserializer);
	        ContainerProperties containerProperties = new ContainerProperties("library-events");
	        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
	        records = new LinkedBlockingQueue<>();
	        container.setupMessageListener((MessageListener<String, LibraryEvent>) records::add);
	        container.start();
	        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
		
    }
	
	@After
	public void cleanup() {
		container.stop();
	}
	@Test
	public void testProducer() throws InterruptedException {
		Book book = new Book(null, "hello-world", "radha krishna");
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Book> requestEntity = new HttpEntity<Book>(book, headers);
		ResponseEntity<LibraryEvent> responseEntity = testRestTemplate.exchange("/books", HttpMethod.POST, requestEntity, LibraryEvent.class);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		LibraryEvent returnedResponse = responseEntity.getBody();
//		Thread.sleep(200);
//		
//		ConsumerRecord<String, LibraryEvent> record = KafkaTestUtils.getSingleRecord(consumer, "library-events");
//		LibraryEvent kafkaEvent = record.value();
//		
//		assertEquals(returnedResponse, kafkaEvent);
		
		Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
      

		JsonSerializer<LibraryEvent> valueSerializer = new JsonSerializer<>();
        Producer<String, LibraryEvent> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), valueSerializer).createProducer();
        
        // Act
        producer.send(new ProducerRecord<>("library-events", returnedResponse));
        producer.flush();
        
		ConsumerRecord<String, LibraryEvent> singleRecord = records.poll(100, TimeUnit.MILLISECONDS);
		LibraryEvent kafkaEvent = singleRecord.value();
		assertEquals(returnedResponse, kafkaEvent);
	}
	
	@Test
	public void testProducerError() {
		Book book = new Book("fake-book-id", "hello-world", "radha krishna");
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Book> requestEntity = new HttpEntity<Book>(book, headers);
		ResponseEntity<LibraryEvent> responseEntity = testRestTemplate.exchange("/books", HttpMethod.POST, requestEntity, LibraryEvent.class);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}
}
