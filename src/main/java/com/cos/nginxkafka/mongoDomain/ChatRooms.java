package com.cos.nginxkafka.mongoDomain;

import com.cos.nginxkafka.dto.ChatRequestDTO;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatrooms")
@Builder
public class ChatRooms {

    @Id
    @Field("_id")
    private String chatroomId;

    @Builder.Default
    private List<ChatRequestDTO> messages = new ArrayList<>();

    public ChatRooms(String chatroomId) {
        this.chatroomId = chatroomId;
    }

}
