package com.cos.nginxkafka.jpaRepository;

import com.cos.nginxkafka.jpaEntity.ChatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepositoryJPA extends JpaRepository<ChatEntity,Long> {
    Page<ChatEntity> findByChatroomId(String chatroomId, Pageable pageable);
    List<ChatEntity> findByChatroomId(String chatroomId);
}
