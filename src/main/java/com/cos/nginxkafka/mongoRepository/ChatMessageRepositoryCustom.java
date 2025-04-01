package com.cos.nginxkafka.mongoRepository;

import com.cos.nginxkafka.mongoDomain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatMessageRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    // cursor 방식 구현
    public List<ChatMessage> findByChatroomIdWithCursor(
            String chatroomId,
            LocalDateTime lastTimestamp,
            int pageSize
    ) {
        // 1. 쿼리 생성
        Query query = new Query();

        // 2. chatroomId 조건 추가
        query.addCriteria(Criteria.where("chatroomId").is(chatroomId));

        // 3. 커서 값(lastTimestamp)이 있을 경우, timestamp < lastTimestamp 조건 추가
        if (lastTimestamp != null) {
            query.addCriteria(Criteria.where("timestamp").lt(lastTimestamp));
        }

        // 4. 정렬 (최신 메시지가 뒤에 오게 DESC)
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"));

        // 5. limit 설정 (한 번에 가져올 문서 수)
        query.limit(pageSize);

        // 6. 실제 DB에서 쿼리 실행
        return mongoTemplate.find(query, ChatMessage.class);
    }
}
