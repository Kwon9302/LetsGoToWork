package com.cos.nginxkafka.service;

import com.cos.nginxkafka.KafkaProducer;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.mongoEntity.ChatEntity;
import com.cos.nginxkafka.jpaEntity.ChatJPAEntity;
import com.cos.nginxkafka.mongoRepository.ChatRepository;
import com.cos.nginxkafka.jpaRepository.ChatRepositoryJPA;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final KafkaProducer kafkaProducer;
    private final ChatRepository chatRepository;

    public void save(ChatRequestDTO chatRequestDTO) {
        log.info("save chatRequestDTO(ChatService) = {}", chatRequestDTO);
        try {
            ChatEntity chatEntity = ChatEntity.builder()
                    .chatroomId(chatRequestDTO.getChatroomId())
                    .sender(chatRequestDTO.getSender())
                    .content(chatRequestDTO.getContent())
                    .build();
            chatRepository.save(chatEntity);
            log.info("✅ [ChatService (MongoDB)] Successfully saved: {}", chatEntity);
        } catch (Exception e) {
            log.error("ChatService  : 저장 실패~");
        }
    }
}
