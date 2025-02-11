package com.cos.nginxkafka.service;

import com.cos.nginxkafka.KafkaProducer;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.mongoDomain.ChatMessage;
import com.cos.nginxkafka.mongoDomain.ChatRooms;
import com.cos.nginxkafka.mongoRepository.ChatMessageRepository;
import com.cos.nginxkafka.mongoRepository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final KafkaProducer kafkaProducer;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * 채팅방을 조회하고 없으면 생성하는 메소드
     */
    private ChatRooms getOrCreateChatRoom(String chatroomId) {
        return chatRoomRepository.findById(chatroomId)
                .orElseGet(() -> {
                    log.info("채팅방 조회 X, 새 채팅방 생성: {}", chatroomId);
                    ChatRooms newChatRoom = ChatRooms.builder()
                            .chatroomId(chatroomId)
                            .build();
                    return chatRoomRepository.save(newChatRoom);
                });
    }

    /**
     * 채팅방에 메시지를 추가하는 메소드
     */
    public void addMessage(ChatRequestDTO chatRequestDTO) {
        // 채팅방이 없으면 생성
        ChatRooms chatRoom = getOrCreateChatRoom(chatRequestDTO.getChatroomId());

        // 메시지 생성
        ChatMessage chatMessage = ChatMessage.builder()
                .chatroomId(chatRoom.getChatroomId())
                .sender(chatRequestDTO.getSender())
                .content(chatRequestDTO.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        try {
            chatMessageRepository.save(chatMessage);
            log.info("✅ [ChatService (MongoDB)] Successfully saved message: {}", chatMessage);
        } catch (Exception e) {
            log.error("ChatService : 메시지 저장 실패", e);
        }
    }


    /**
     * 채팅방의 메시지를 페이징하여 조회하는 메소드
     */
    public Page<ChatMessage> getMessages(String chatroomId, Pageable pageable) {
        return chatMessageRepository.findByChatroomId(chatroomId, pageable);
    }


}
