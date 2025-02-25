package com.cos.nginxkafka.service;

import com.cos.nginxkafka.es.ChatMessageIndex;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.mongoDomain.ChatMessage;
import com.cos.nginxkafka.mongoDomain.ChatRooms;
import com.cos.nginxkafka.esRepository.ChatMessageSearchRepository;
import com.cos.nginxkafka.mongoRepository.ChatMessageRepository;
import com.cos.nginxkafka.mongoRepository.ChatRoomRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageSearchRepository chatMessageSearchRepository;
    private final S3Service s3Service;

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
        getOrCreateChatRoom(chatRequestDTO.getChatroomId());

        // 🔹 Builder 패턴을 사용하여 객체 생성
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(chatRequestDTO.getSender())
                .chatroomId(chatRequestDTO.getChatroomId())
                .content(chatRequestDTO.getContent())
                .timestamp(LocalDateTime.now())
                .fileUrl(null)
                .build();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String time = LocalDateTime.now().format(formatter);
        // ES 추가 저장
        ChatMessageIndex indexMessage = new ChatMessageIndex().builder()
                .sender(chatRequestDTO.getSender())
                .chatroomId(chatRequestDTO.getChatroomId())
                .content(chatRequestDTO.getContent())
                .timestamp(OffsetDateTime.now())
                .build();

        try {
            chatMessageRepository.save(chatMessage);

            log.info("✅ [ChatService (MongoDB)] Successfully saved message: {}", chatMessage);
        } catch (Exception e) {
            log.error("ChatService : MongoDB 메시지 저장 실패", e);
        }

        try {
            chatMessageSearchRepository.save(indexMessage);
            log.info("✅ [ChatService (Elastic Search)] Successfully saved message: {}", chatMessage);
        } catch (Exception e) {
            log.error("ChatService : ES 메시지 저장 실패", e);
        }
    }

    /**
     * 채팅방의 메시지(mongoDB)를 페이징하여 조회하는 메소드
     */
    public Page<ChatMessage> getMessages(String chatroomId, Pageable pageable) {
        return chatMessageRepository.findByChatroomId(chatroomId, pageable);
    }

    /**
     * content 검색하기
     * @param keyword
     * @return
     */
    public List<ChatMessageIndex> searchChatMessages(String keyword) {
        List<ChatMessageIndex> chatMessageIndexList = chatMessageSearchRepository.findByContentContaining(keyword);

        if(chatMessageIndexList == null || chatMessageIndexList.size() == 0) {
            log.info("=========== ES에 데이터가 없읍니다 =========");
            return null;
        }
        return chatMessageSearchRepository.findByContentContaining(keyword);
    }

    /**
     * 파일 업로드시 저장하는 메서드.
     * @param chatRequestDTO
     * @param file
     */
    public String saveFile(ChatRequestDTO chatRequestDTO,MultipartFile file) {
        String fileUrl = null;

        // 첨부파일이 있으면 S3에 업로드
        if (file != null && !file.isEmpty()) {
            try {
                fileUrl = s3Service.uploadFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 🔹 Builder 패턴을 사용하여 객체 생성
        ChatMessage chatMessage = ChatMessage.builder()
                .chatroomId(chatRequestDTO.getChatroomId())
                .sender(chatRequestDTO.getSender())
                .content(chatRequestDTO.getContent())
                .timestamp(LocalDateTime.now())
                .fileUrl(fileUrl)
                .build();

        chatMessageRepository.save(chatMessage);
        return fileUrl;
    }

    /**
     * ✅ 파일 다운로드 (S3에서 파일 가져오기)
     */
    public Resource getFileFromS3(String fileName) {
        return (Resource) s3Service.downloadFile(fileName);
    }
}
