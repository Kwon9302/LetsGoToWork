package com.cos.nginxkafka.service.kafkaService;

import com.cos.nginxkafka.dto.ChatRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DltConsumer {
    private final KafkaTemplate<String, ChatRequestDTO> kafkaTemplate;
    private static final int MAX_RETRY = 3;

    @KafkaListener(topics = "message.DLT", groupId = "dlt-handler", errorHandler = "dltErrorHandler")
    public void handleDlt(ChatRequestDTO msg,
                          @Header(KafkaHeaders.DLT_ORIGINAL_TOPIC) String originalTopic,
                          @Header(value = "retry-count", required = false, defaultValue = "0") int retryCnt) {

        log.warn("⚠️  DLT 수신: {}, retryCnt={}", msg, retryCnt);

        // ----- 재시도 여부 판단(parking-lot 미구현) -----
        if (retryCnt >= MAX_RETRY) {
            log.error("❌ 최대 재시도 초과. parking-lot 으로 이동");
//            kafkaTemplate.send("message.PARKING_LOT", msg.getChatroomId(), msg);
            return;
        }

        Message<ChatRequestDTO> retryMessage = MessageBuilder
                .withPayload(msg)
                .setHeader(KafkaHeaders.TOPIC, originalTopic)
                .setHeader(KafkaHeaders.KEY, msg.getChatroomId())
                .setHeader("retry-count", retryCnt + 1)
                .build();

        kafkaTemplate.send(retryMessage);
        log.info("🔄 재전송 완료 ({}→{}), retryCount={}", "message.DLT", originalTopic, retryCnt + 1);
    }
}
