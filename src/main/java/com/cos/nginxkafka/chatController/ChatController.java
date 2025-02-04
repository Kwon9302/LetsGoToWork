package com.cos.nginxkafka.chatController;

import com.cos.nginxkafka.KafkaConsumer;
import com.cos.nginxkafka.KafkaProducer;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaService.ChatServiceJpa;
import com.cos.nginxkafka.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        chatService.save(message);
        chatServiceJpa.save(message);
    }

}
