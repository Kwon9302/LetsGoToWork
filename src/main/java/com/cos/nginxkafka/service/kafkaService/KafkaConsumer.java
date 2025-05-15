package com.cos.nginxkafka.service.kafkaService;

import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.es.ChatMessageIndex;
import com.cos.nginxkafka.esRepository.ChatMessageSearchRepository;
import com.cos.nginxkafka.service.ChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final ChatMessageSearchRepository chatMessageSearchRepository;

    /**
     * 채팅 전송
     * @param message
     */

    @KafkaListener(topics = "test-topic",groupId = "#{T(java.util.UUID).randomUUID().toString()}", concurrency = "3", properties = {"max.poll.interval.ms=30000"})
    public void consumeMessage(@Payload ChatRequestDTO message) {
        log.info("Received message: " + message);

        try {
            String destination = "/topic/chat/" + message.getChatroomId();
            simpMessagingTemplate.convertAndSend(destination, message);

        } catch (Exception e) {
            log.error("Error parsing message: ", e);
        }
    }


    /**
     * 채팅 저장 Consumer
     * @param message
     */
    @Transactional
    @KafkaListener(topics = "test-topic", groupId = "chat-group-save", concurrency = "3")
    public void consumeEsMessage(@Payload ChatRequestDTO message, Acknowledgment ack) {
        chatService.addMessage(message);
        ChatMessageIndex chatMessageIndex = ChatMessageIndex.builder()
                .chatroomId(message.getChatroomId())
                .sender(message.getSender())
                .content(message.getContent())
                .timestamp(OffsetDateTime.now())
                .build();

        chatMessageSearchRepository.save(chatMessageIndex);
//        chatServiceJpa.save(message);

        ack.acknowledge();
        log.info("Received esMessage: " + message);
    }
}
