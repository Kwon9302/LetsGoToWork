package com.cos.nginxkafka.mongoRepository;

import com.cos.nginxkafka.jpaEntity.ChatEntity;
import com.cos.nginxkafka.mongoDomain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatroomId(String chatroomId);
    Page<ChatMessage> findByChatroomId(String chatroomId, Pageable pageable);
    ChatMessage findByFileUrl(String fileUrl);

    @Query("{ 'chatroomId': ?0, 'content': { $regex: ?1, $options: 'i' } }")
    List<ChatMessage> searchByChatroomIdAndContentRegex(String chatroomId, String keyword);
}
