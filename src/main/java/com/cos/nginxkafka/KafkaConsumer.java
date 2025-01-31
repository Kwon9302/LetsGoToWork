package com.cos.nginxkafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "test-topic", groupId = "kafka-group", concurrency = "3")
    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
