package com.cos.nginxkafka.chatController;

import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaService.ChatServiceJpa;
import com.cos.nginxkafka.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 쓰기 성능 테스트를 위한 Controller
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatSaveController {
    private final ChatService chatService;
    private final ChatServiceJpa chatServiceJpa;

    @PostMapping("/save/message/mongo")
    public void saveMessageByMongoDB(@RequestBody ChatRequestDTO message) {
        chatService.addMessage(message); // MongoDB 저장
    }

    @PostMapping("/save/message/mysql")
    public void saveMessageByMysql(@RequestBody ChatRequestDTO message) {
        chatServiceJpa.save(message); // MySQL 저장
    }

    /**
     * 파일저장 메서드
     */
    @PostMapping("/save/file")
    public ResponseEntity<Map<String, String>> saveFile(@Payload ChatRequestDTO chatRequestDTO,
                                                        @RequestBody MultipartFile file) throws IOException {
        String fileUrl = chatService.saveFile(chatRequestDTO, file);
        Map<String, String> response = new HashMap<>();
        response.put("url", fileUrl);
        return ResponseEntity.ok(response);
    }


}
