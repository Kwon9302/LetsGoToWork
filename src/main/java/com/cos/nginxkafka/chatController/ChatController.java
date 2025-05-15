package com.cos.nginxkafka.chatController;

import com.cos.nginxkafka.service.kafkaService.KafkaProducer;
import com.cos.nginxkafka.es.ChatMessageIndex;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaEntity.ChatEntity;
import com.cos.nginxkafka.jpaService.ChatServiceJpa;
import com.cos.nginxkafka.mongoDomain.ChatMessage;
import com.cos.nginxkafka.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final KafkaProducer kafkaProducer;
    private final ChatService chatService;
    private final ChatServiceJpa chatServiceJpa;

    /**
     * Websocket Mapping
     * @param message
     */
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatRequestDTO message) {
        log.info("📩 [ChatController] Received message(chatController): Sender={}, ChatroomId={}, Content={}",
                message.getSender(), message.getChatroomId(), message.getContent());

        String topic = "test-topic";
        kafkaProducer.sendMessage(topic, message.getContent(), message.getSender(), message.getChatroomId());

    }

    /**
     * MongoDB Offset Pagenation
     * @param chatroomId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/history/{chatroomId}")
    public ResponseEntity<Page<ChatMessage>> getChatHistory(@PathVariable String chatroomId,@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "100") int size) {
        log.info("📜 [ChatController] Fetching chat history for chatroomId={}", chatroomId);

        // 예시: timestamp 기준 내림차순 정렬 (최신 메시지가 먼저 나오도록)
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessage> messages = chatService.getMessages(chatroomId, pageable);

        return ResponseEntity.ok(messages);
    }

    /**
     * MongoDB 채팅 내역 Read(Pagenation X)
     * @param chatroomId
     * @return
     */
    @GetMapping("/history/nop/{chatroomId}") // mongo에서 불러오기
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String chatroomId) {

        List<ChatMessage> messages = chatService.getMessages(chatroomId);
        return ResponseEntity.ok(messages);
    }

    /**
     * MySQL 채팅 내역 Read(Pagenation X)
     * @param chatroomId
     * @return
     */
    @GetMapping("/history2/nop/{chatroomId}")
    public ResponseEntity<List<ChatEntity>> getChatHistoryNop(@PathVariable String chatroomId) {
        List<ChatEntity> chatHistory = chatServiceJpa.findByChatroomIdNoPaging(chatroomId);
        log.info("chatList count={}", chatHistory.size());
        return ResponseEntity.ok(chatHistory);
    }

    /**
     * MySQL Offset방식 Pagenation
     * @param chatroomId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/history2/{chatroomId}")
    public ResponseEntity<Page<ChatEntity>> getChatHistory2(@PathVariable String chatroomId, @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatEntity> chatHistory = chatServiceJpa.findByChatroomIdPaging(chatroomId,pageable);

        if (chatHistory == null || chatHistory.isEmpty()) {
            log.warn("⚠️ [ChatController] No chat history found for chatroomId={}", chatroomId);
            return ResponseEntity.noContent().build(); // 204 No Content 반환
        }
        return ResponseEntity.ok(chatHistory);
    }


    /**
     * MySQL Cursor방식 Pagenation
     * @param chatroomId
     * @param lastMessageId
     * @return
     */
    @GetMapping("/history2/cursor/{chatroomId}")
    public ResponseEntity<List<ChatEntity>> getChatHistory2(@PathVariable String chatroomId,
                                                               @RequestParam(required = false) Long lastMessageId) {
        int pageSize = 20;
        List<ChatEntity> chatHistory = chatServiceJpa.findByChatroomId(chatroomId,lastMessageId,pageSize);

        if (chatHistory == null || chatHistory.isEmpty()) {
            log.warn("⚠️ [ChatController] No chat history found for chatroomId={}", chatroomId);
            return ResponseEntity.noContent().build(); // 204 No Content 반환
        }
        return ResponseEntity.ok(chatHistory);
    }

    /**
     * 몽고DB Cursor방식 Pagination
     * @param chatroomId
     * @param lastTime
     * @return
     */
    @GetMapping("/history/cursor/{chatroomId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory222(@PathVariable String chatroomId,
                                                               @RequestParam(required = false) String lastTime
                                                               ){
        int pageSize = 20;

        // lastTime이 null이면 첫 페이지 요청으로 간주
        LocalDateTime cursor = null;
        if (lastTime != null) {
            // 문자열을 LocalDateTime 등으로 변환
            // 예: 2025-03-15T10:20:30
            cursor = LocalDateTime.parse(lastTime);
        }

        // 커서 기반으로 메시지 조회
        List<ChatMessage> messages = chatService.getChatMessages(chatroomId, cursor, pageSize);
        return ResponseEntity.ok(messages);
    }

    /**
     * 키워드(Elastic Search)를 이용한 채팅 검색
     * @param keyword
     * @return
     */
    @GetMapping("/search/chat")
    public ResponseEntity<List<ChatMessageIndex>> searchMessages(@RequestParam String keyword) {

        String chatroomId = "123";
        List<ChatMessageIndex> results = chatService.searchChatMessages(chatroomId, keyword);
        log.info("검색된 ES 채팅 데이터 수 : {}", results.size());
        return ResponseEntity.ok(results);
    }

    /**
     * Mongodb를 이용한 채팅 검색
     * @param keyword
     * @return
     */
    @GetMapping("/search/chat/mongo")
        public ResponseEntity<List<ChatMessageIndex>> searchMessagesMongo(@RequestParam String keyword) {
        String chatroomId = "123";
        List<ChatMessageIndex> results = chatService.searchChatMessages(chatroomId, keyword);
        log.info("검색된 Mongo 채팅 데이터 수 : {}", results.size());
        return ResponseEntity.ok(results);
    }
}
