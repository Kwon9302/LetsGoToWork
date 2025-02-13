package com.cos.nginxkafka.chatController;

import com.cos.nginxkafka.KafkaProducer;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaService.ChatServiceJpa;
import com.cos.nginxkafka.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 쓰기 성능 테스트를 위한 Controller
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatSaveController {
    private final ChatService chatService;
    private final ChatServiceJpa chatServiceJpa;

    @PostMapping("/save/message/mongo")
    public void saveMessageByMongoDB(@RequestBody ChatRequestDTO message) {

        chatService.addMessage(message); // MongoDB 저장
    }

    @PostMapping("/save/message/mysql")
    public void saveMessageByMysql(@RequestBody ChatRequestDTO message) {

        chatServiceJpa.save(message); // MySQL 저장
    }
}
