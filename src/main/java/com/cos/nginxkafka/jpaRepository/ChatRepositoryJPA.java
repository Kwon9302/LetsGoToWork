package com.cos.nginxkafka.jpaRepository;

import com.cos.nginxkafka.jpaEntity.ChatJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepositoryJPA extends JpaRepository<ChatJPAEntity,Long> {
}
