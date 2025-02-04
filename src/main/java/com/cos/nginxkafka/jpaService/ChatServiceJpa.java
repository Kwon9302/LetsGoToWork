package com.cos.nginxkafka.jpaService;

import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaEntity.ChatJPAEntity;
import com.cos.nginxkafka.jpaRepository.ChatRepositoryJPA;
import com.cos.nginxkafka.mongoEntity.ChatEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
