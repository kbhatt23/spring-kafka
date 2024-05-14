package com.learning.kafka.productsservice.services;

import com.learning.kafka.events.ProductCreatedEvent;
import com.learning.kafka.productsservice.dtos.ProductDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements  ProductService{

    @Autowired
    private  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    private final Logger LOGGER  = LoggerFactory.getLogger(this.getClass());

    @Override
    public String createProduct(ProductDTO productDTO) throws Exception {
        String productID = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productID, productDTO.name(),
                productDTO.price(),productDTO.quantity());

        LOGGER.info("Trying to publish ProductCreatedEvent for product:{}",productID);

        ProducerRecord<String, ProductCreatedEvent> producerRecord = new ProducerRecord("product-created-topic", productID, productCreatedEvent);
        producerRecord.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        CompletableFuture<SendResult<String, ProductCreatedEvent>> futureEvent = kafkaTemplate.send(producerRecord);
        //blocking event
        SendResult<String, ProductCreatedEvent> sendResult = futureEvent.get();

        LOGGER.info("Partition: " + sendResult.getRecordMetadata().partition());
        LOGGER.info("Topic: " + sendResult.getRecordMetadata().topic());
        LOGGER.info("Offset: " + sendResult.getRecordMetadata().offset());

        LOGGER.info("***** Returning product id");

        return productID;

    }
}
