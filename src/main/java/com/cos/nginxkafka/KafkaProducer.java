package com.cos.nginxkafka;

import lombok.extern.slf4j.Slf4j;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, ChatRequestDTO> kafkaTemplate;

    public void sendMessage(String topic, String content, String senderId, String chatroomId) {
        try {
            ChatRequestDTO message = new ChatRequestDTO();
            message.setContent(content);
            message.setSender(senderId);
            message.setChatroomId(chatroomId);

            log.info("Sent Kafka message(KafkaProducer): {}", message);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            log.error("Error sending Kafka message", e);
        }
    }
}
