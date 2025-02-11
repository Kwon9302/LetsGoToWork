package com.cos.nginxkafka.mongoRepository;

import com.cos.nginxkafka.mongoDomain.ChatRooms;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRooms, String> {
//    ChatRooms findByChatroomId(String chatroomId);
}
