package com.cos.nginxkafka.chatController;

import com.cos.nginxkafka.service.kafkaService.KafkaProducer;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import com.cos.nginxkafka.jpaService.ChatServiceJpa;
import com.cos.nginxkafka.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatSaveController {
    private final ChatService chatService;
    private final ChatServiceJpa chatServiceJpa;
    private final KafkaProducer kafkaProducer;

    /**
     * MongoDB 채팅 저장
     * @param message
     */
    @PostMapping("/save/message/mongo")
    public void saveMessageByMongoDB(@RequestBody ChatRequestDTO message) {
        chatService.addMessage(message); // MongoDB 저장
    }

    /**
     * MySQL 채팅 저장
     * @param message
     */
    @PostMapping("/save/message/mysql")
    public void saveMessageByMysql(@RequestBody ChatRequestDTO message) {
        chatServiceJpa.save(message); // MySQL 저장
    }

    /**
     * MySQL 채팅 저장(Kafka)
     * @param message
     */
    @PostMapping("/save/message/mysql2")
    public void saveMessageByMysql2(@RequestBody ChatRequestDTO message) {
        String topic = "test-topic";
        kafkaProducer.sendMessage(topic, message.getContent(), message.getSender(), message.getChatroomId());
    }

    /**
     * 파일저장 메서드
     */
//    @PostMapping("/save/file")
//    public ResponseEntity<Map<String, String>> saveFile(@Payload ChatRequestDTO chatRequestDTO,
//                                                        @RequestBody MultipartFile file) throws IOException {
//        String fileUrl = chatService.saveFile(chatRequestDTO, file);
//        Map<String, String> response = new HashMap<>();
//        response.put("url", fileUrl);
//        return ResponseEntity.ok(response);
//    }


}
