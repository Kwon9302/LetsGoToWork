package com.cos.nginxkafka.jpaRepository;

import com.cos.nginxkafka.jpaEntity.ChatJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepositoryJPA extends JpaRepository<ChatJPAEntity,Long> {
    List<ChatJPAEntity> findByChatroomId(String chatroomId);
}
