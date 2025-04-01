package com.cos.nginxkafka.jpaService;

import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaEntity.ChatEntity;
import com.cos.nginxkafka.jpaRepository.ChatRepositoryJPA;
import com.cos.nginxkafka.jpaRepository.ChatRepositoryJdbc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceJpa {
    private final ChatRepositoryJPA chatRepositoryJPA;
    private final ChatRepositoryJdbc chatRepositoryJdbc;

    /**
     * MySQL 채팅 저장
     * @param chatRequestDTO
     */
    public void save(ChatRequestDTO chatRequestDTO) {
            ChatEntity chatJPAEntity = ChatEntity.builder()
                    .chatroomId(chatRequestDTO.getChatroomId())
                    .sender(chatRequestDTO.getSender())
                    .content(chatRequestDTO.getContent())
                    .timestamp(LocalDateTime.now())
                    .fileUrl(null)
                    .build();
            chatRepositoryJdbc.save(chatJPAEntity);

            log.info("Chat Saved Successfully ( MySQL )");
    }

    /**
     * MySQL ChatroomId기준 채팅 조회
     * @param chatroomId
     * @param pageable
     * @return
     */
    public Page<ChatEntity> findByChatroomIdPaging(String chatroomId, Pageable pageable) {
        return chatRepositoryJPA.findByChatroomId(chatroomId, pageable);
    }

    /**
     * MySQL ChatroomId기준 채팅 조회(Pagenation X)
     * @param chatroomId
     * @return
     */
    public List<ChatEntity> findByChatroomIdNoPaging(String chatroomId) {
        log.info("No Paging chatroomId: {}", chatroomId);
        return chatRepositoryJPA.findByChatroomId(chatroomId);
    }

    /**
     * MySQL 채팅 Cursor방식 조회
     * @param chatroomId
     * @param lastMessageId
     * @param pageSize
     * @return
     */
    public List<ChatEntity> findByChatroomId(String chatroomId, Long lastMessageId, int pageSize) {

        return chatRepositoryJdbc.findByChatroomIdWithCursor(chatroomId, lastMessageId,pageSize);
    }
}
