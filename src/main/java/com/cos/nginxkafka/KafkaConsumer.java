package com.cos.nginxkafka;

import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "test-topic", groupId = "#{T(java.util.UUID).randomUUID().toString()}", concurrency = "3") // 스레드 수
    public void consumeMessage(@Payload ChatRequestDTO message) {
        log.info("Received message: " + message);

        // JSON 형태로 메시지를 파싱
        try {
            // 올바른 WebSocket 경로로 메시지 전송 (채팅방 ID 포함)
            String destination = "/topic/chat/" + message.getChatroomId();
            log.info("Sent message to WebSocket(KafkaListener): " + destination);
            simpMessagingTemplate.convertAndSend(destination, message);
        } catch (Exception e) {
            log.error("Error parsing message: ", e);
        }
    }
}
