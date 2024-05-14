package com.learning.kafka.emailnotificationservice.kafkalisteners;

import com.learning.kafka.emailnotificationservice.entities.ProcessedEventEntity;
import com.learning.kafka.emailnotificationservice.exceptions.KafkaRecoverableException;
import com.learning.kafka.emailnotificationservice.exceptions.KafkaUnRecoverableException;
import com.learning.kafka.emailnotificationservice.repositories.ProcessedEventRepository;
import com.learning.kafka.events.ProductCreatedEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.HTML;
import java.util.Set;

@Service
public class EmailNotificationListener {

    private final Logger LOGGER  = LoggerFactory.getLogger(this.getClass());

    private static final Set<HttpStatusCode> RECOVERABLE_STATUS_CODES = Set.of(HttpStatus.TOO_MANY_REQUESTS,
            HttpStatus.GATEWAY_TIMEOUT);


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProcessedEventRepository processedEventRepository;

    @KafkaListener(topics = "product-created-topic")
    @Transactional
    public void sendEmail(@Payload ProductCreatedEvent productCreatedEvent, @Header("messageId") String messageId,
                          @Header(KafkaHeaders.RECEIVED_KEY) String messageKey){
        LOGGER.info("sending email for product:{}, messageID:{}",productCreatedEvent.getProductID(),messageId);

        ProcessedEventEntity processedEventEntity = processedEventRepository.findById(messageId).orElse(null);
        if(processedEventEntity != null){
            LOGGER.info("Found a duplicate message id: {}", processedEventEntity.getMessageId());
            return;
        }

        try {
            String url = "http://localhost:1000/send-email/success";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            LOGGER.info("sendEmail: response code:{}",response.getStatusCode());
            if(response.getStatusCode().equals(HttpStatus.OK)){
                LOGGER.info("sendEmail: success response received:{}",response.getBody());
            }

        } catch (HttpClientErrorException ex) {
            if (RECOVERABLE_STATUS_CODES.contains(ex.getStatusCode())) {
                LOGGER.error("HttpClientErrorException occurred which can be retried :{}",ex);
                throw new KafkaRecoverableException(ex);
            } else {
                LOGGER.error("HttpClientErrorException occurred which can't be retried :{}",ex);
                throw new KafkaUnRecoverableException(ex);
            }
        } catch (ResourceAccessException ex) {
            //service down/ network timeouts
            LOGGER.error("ResourceAccessException occurred which can be retried :{}",ex);
            throw new KafkaRecoverableException(ex);
        } catch (Exception ex) {
            LOGGER.error("Exception occurred which can't be retried :{}",ex);
            throw new KafkaUnRecoverableException(ex);
        }

        try {
            processedEventRepository.save(new ProcessedEventEntity(messageId, messageKey));
        } catch (DataIntegrityViolationException ex) {
            throw new KafkaUnRecoverableException(ex);
        }
    }
}
