package com.learning.kafka.emailnotificationservice.exceptions;

public class KafkaUnRecoverableException extends RuntimeException{
    public KafkaUnRecoverableException(String message) {
        super(message);
    }

    public KafkaUnRecoverableException(Throwable cause) {
        super(cause);
    }
}
