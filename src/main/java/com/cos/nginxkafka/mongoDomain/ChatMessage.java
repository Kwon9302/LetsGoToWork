package com.cos.nginxkafka.mongoDomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
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

    // sender에 인덱스 추가 (사용자별 검색 최적화)
    @Indexed
    private String sender;

//    @Indexed
    @TextIndexed
    private String content;

    @Indexed(direction = IndexDirection.DESCENDING)
    private LocalDateTime timestamp;
    private String fileUrl; // S3에 저장된 첨부파일 URL
}
