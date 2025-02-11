package com.cos.nginxkafka.mongoDomain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatmessages") // MongoDB에서 해당 Collection을 사용한다.
@Builder
public class ChatMessage {
    @Id
    private String id;
    private String chatroomId; // 어느 채팅방의 메시지인지 참조
    private String sender;
    private String content;
    private LocalDateTime timestamp;
}
