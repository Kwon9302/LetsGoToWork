package com.cos.nginxkafka.jpaEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(indexes = {
        @Index(name = "idx_chatroom_created_at", columnList = "chatroomId")
})
public class ChatJPAEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatroomId;
    private String sender;
    private String content;
}
