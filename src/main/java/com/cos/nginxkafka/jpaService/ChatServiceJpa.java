package com.cos.nginxkafka.jpaService;

import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaEntity.ChatJPAEntity;
import com.cos.nginxkafka.jpaRepository.ChatRepositoryJPA;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceJpa {
    private final ChatRepositoryJPA chatRepositoryJPA;

    public void save(ChatRequestDTO chatRequestDTO) {
        try {
            ChatJPAEntity chatJPAEntity = ChatJPAEntity.builder()
                    .chatroomId(chatRequestDTO.getChatroomId())
                    .sender(chatRequestDTO.getSender())
                    .content(chatRequestDTO.getContent())
                    .build();
            chatRepositoryJPA.save(chatJPAEntity);

            log.info("Chat Saved Successfully ( MySQL )");
        } catch (Exception e) {
            log.error("ChatService  : 저장 실패~");
        }
    }

    public List<ChatRequestDTO> findByChatroomId(String chatroomId) {
        chatroomId = "123";
        long queryStart = System.currentTimeMillis();
        List<ChatJPAEntity> chatEntities = chatRepositoryJPA.findByChatroomId(chatroomId);
//        List<ChatJPAEntity> chatEntities = chatRepositoryJPA.findAll();
        if (chatEntities == null || chatEntities.isEmpty()) {
            return Collections.emptyList();
        }
        long queryEnd = System.currentTimeMillis();
        log.info("Mysql 쿼리 실행 시간: {}ms", queryEnd - queryStart);
        return chatEntities.stream()
                .map(chatEntity -> ChatRequestDTO.builder()
                        .chatroomId(chatEntity.getChatroomId())
                        .sender(chatEntity.getSender())
                        .content(chatEntity.getContent())
                        .build())
                .collect(Collectors.toList());
    }
}
