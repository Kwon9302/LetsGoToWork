package com.cos.nginxkafka.mongoRepository;

import com.cos.nginxkafka.mongoEntity.ChatEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<ChatEntity, String> {

    List<ChatEntity> findByChatroomId(String chatroomId);
}
