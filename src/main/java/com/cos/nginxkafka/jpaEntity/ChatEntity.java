package com.cos.nginxkafka.jpaEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "chatjpaentity")
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatroomId;
    private String sender;
    private String content;

    private LocalDateTime timestamp;
    private String fileUrl;
}
