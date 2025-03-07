package com.cos.nginxkafka.mongoRepository;

import com.cos.nginxkafka.mongoDomain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    Page<ChatMessage> findByChatroomId(String chatroomId, Pageable pageable);
    ChatMessage findByFileUrl(String fileUrl);

}
