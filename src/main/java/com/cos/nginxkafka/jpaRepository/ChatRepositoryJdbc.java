package com.cos.nginxkafka.jpaRepository;

import com.cos.nginxkafka.jpaEntity.ChatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ChatRepositoryJdbc {

    private final JdbcTemplate jdbcTemplate;

    public List<ChatEntity> findByChatroomId(String chatroomId) {
        String querySql = "SELECT id, chatroom_id AS chatroomId, sender, content, timestamp, file_url AS fileUrl " +
                "FROM chatjpaentity WHERE chatroom_id = ?";

        return jdbcTemplate.query(
                querySql,
                new BeanPropertyRowMapper<>(ChatEntity.class),
                chatroomId
        );
    }

    public List<ChatEntity> findByChatroomIdWithCursor(String chatroomId, Long lastMessageId, int pageSize) {
        String querySql = "SELECT id, chatroom_id AS chatroomId, sender, content, file_url AS fileUrl " +
                "FROM chatjpaentity " +
                "WHERE chatroom_id = ? " +
                (lastMessageId != null ? "AND id < ? " : "") +
                "ORDER BY id DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(
                querySql,
                new BeanPropertyRowMapper<>(ChatEntity.class),
                lastMessageId != null ? new Object[]{chatroomId, lastMessageId, pageSize} : new Object[]{chatroomId, pageSize}
        );
    }

    // 단일 ChatJPAEntity 저장
    public int save(ChatEntity entity) {
        String sql = "INSERT INTO chatjpaentity (chatroom_id, sender, content, file_url, timestamp) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                entity.getChatroomId(),
                entity.getSender(),
                entity.getContent(),
                entity.getFileUrl(),
                entity.getTimestamp()
        );
    }
}
