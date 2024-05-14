package com.learning.kafka.emailnotificationservice.exceptions;

public class KafkaRecoverableException extends RuntimeException{
    public KafkaRecoverableException(String message) {
        super(message);
    }

    public KafkaRecoverableException(Throwable cause) {
        super(cause);
    }
}
