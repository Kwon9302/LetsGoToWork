package com.cos.nginxkafka.chatController;

import com.cos.nginxkafka.KafkaProducer;
import com.cos.nginxkafka.es.ChatMessageIndex;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaService.ChatServiceJpa;
import com.cos.nginxkafka.mongoDomain.ChatMessage;
import com.cos.nginxkafka.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            chatService.addMessage(message); // MongoDB 저장
            chatServiceJpa.save(message); // MySQL 저장
    }



    @GetMapping("/history/{chatroomId}") // mongo에서 불러오기
    public ResponseEntity<Page<ChatMessage>> getChatHistory(@PathVariable String chatroomId,@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "50") int size) {
        log.info("📜 [ChatController] Fetching chat history for chatroomId={}", chatroomId);

        // 예시: timestamp 기준 내림차순 정렬 (최신 메시지가 먼저 나오도록)
        PageRequest pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<ChatMessage> messages = chatService.getMessages(chatroomId, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/history2/{chatroomId}") // mysql에서 불러오기
    public ResponseEntity<List<ChatRequestDTO>> getChatHistory2(@PathVariable String chatroomId) {
//        log.info("📜 [ChatController] Fetching chat history for chatroomId={}", chatroomId);

        List<ChatRequestDTO> chatHistory = chatServiceJpa.findByChatroomId(chatroomId);

        if (chatHistory == null || chatHistory.isEmpty()) {
            log.warn("⚠️ [ChatController] No chat history found for chatroomId={}", chatroomId);
            return ResponseEntity.noContent().build(); // 204 No Content 반환
        }

        return ResponseEntity.ok(chatHistory);
    }

    /**
     * 키워드(Elastic Search)를 이용한 채팅 검색
     * @param keyword
     * @return
     */
    @GetMapping("/search/chat")
    public ResponseEntity<List<ChatMessageIndex>> searchMessages(@RequestParam String keyword) {
        List<ChatMessageIndex> results = chatService.searchChatMessages(keyword);
        log.info("=========== ES로 Searching하기!!! === Keyword={}", keyword);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/savefile")
    public ResponseEntity<Map<String, String>> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("sender") String sender,
            @RequestParam("chatroomId") String chatroomId,
            @RequestParam("content") String content) {
        try {
            ChatRequestDTO chatRequestDTO = ChatRequestDTO.builder()
                    .sender(sender)
                    .chatroomId(chatroomId)
                    .content(content)
                    .timestamp(LocalDateTime.now())  // 시간 추가
                    .build();

            String fileUrl = chatService.saveFile(chatRequestDTO, file);

            Map<String, String> response = new HashMap<>();
            response.put("url", "/download?fileName=" + fileUrl);
            response.put("timestamp", chatRequestDTO.getTimestamp().toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
