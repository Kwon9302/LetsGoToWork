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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ChatRequestDTO> findByChatroomId(String chatroomId) {
        chatroomId = "123";
        List<ChatEntity> chatEntities = chatRepository.findByChatroomId(chatroomId);
        if (chatEntities == null || chatEntities.isEmpty()) {
            return Collections.emptyList();
        }
        return chatEntities.stream()
                .map(chatEntity -> ChatRequestDTO.builder()
                        .chatroomId(chatEntity.getChatroomId())
                        .sender(chatEntity.getSender())
                        .content(chatEntity.getContent())
                        .build())
                .collect(Collectors.toList());

    }
}
