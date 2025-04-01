package com.cos.nginxkafka.mongoDomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatmessages")
@CompoundIndexes({
        @CompoundIndex(name = "chatroom_content_idx", def = "{'chatroomId': 1, 'content': 'text'}")
})
@Builder
public class ChatMessage {

    @MongoId
    private String id;

    @Indexed
    private String chatroomId; // 어느 채팅방의 메시지인지 참조

    private String sender;

//    @Indexed
    @TextIndexed
    private String content;

    private LocalDateTime timestamp;
    private String fileUrl; // S3에 저장된 첨부파일 URL
}
