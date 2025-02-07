package com.cos.nginxkafka.chatController;

import com.cos.nginxkafka.KafkaConsumer;
import com.cos.nginxkafka.KafkaProducer;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaService.ChatServiceJpa;
import com.cos.nginxkafka.mongoEntity.ChatEntity;
import com.cos.nginxkafka.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final KafkaProducer kafkaProducer;
    private final ChatService chatService;
    private final ChatServiceJpa chatServiceJpa;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatRequestDTO message) {
        log.info("📩 [ChatController] Received message(chatController): Sender={}, ChatroomId={}, Content={}",
                message.getSender(), message.getChatroomId(), message.getContent());

        String topic = "test-topic";
        kafkaProducer.sendMessage(topic, message.getContent(), message.getSender(), message.getChatroomId());
        chatService.save(message); // MongoDB 저장
        chatServiceJpa.save(message); // MySQL 저장
    }

    @GetMapping("/history/{chatroomId}")
    public ResponseEntity<List<ChatRequestDTO>> getChatHistory(@PathVariable String chatroomId) {
        log.info("📜 [ChatController] Fetching chat history for chatroomId={}", chatroomId);

        List<ChatRequestDTO> chatHistory = chatService.findByChatroomId(chatroomId);

        if (chatHistory == null || chatHistory.isEmpty()) {
            log.warn("⚠️ [ChatController] No chat history found for chatroomId={}", chatroomId);
            return ResponseEntity.noContent().build(); // 204 No Content 반환
        }

        return ResponseEntity.ok(chatHistory);
    }

}
