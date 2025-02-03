package com.cos.nginxkafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "test-topic", groupId = "kafka-group", concurrency = "3")
    public void consumeMessage(String message) {
        log.info("Consumer message : {}",message);
    }
}
