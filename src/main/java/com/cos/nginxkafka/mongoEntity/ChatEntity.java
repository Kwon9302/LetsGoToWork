package com.cos.nginxkafka.mongoEntity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "chat_messages")
@Builder
public class ChatEntity {
    @Id
    private String id;
    private String chatroomId;
    private String sender;
    private String content;
}
