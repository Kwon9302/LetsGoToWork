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

    @KafkaListener(topics = "message.DLT", groupId = "dlt-handler")
    public void handleDlt(ChatRequestDTO msg,
                          @Header(KafkaHeaders.DLT_ORIGINAL_TOPIC) String originalTopic,
                          @Header(KafkaHeaders.DLT_ORIGINAL_PARTITION) int partition,
                          @Header(KafkaHeaders.DLT_ORIGINAL_OFFSET) long offset,
                          @Header(value = "retry-count", required = false, defaultValue = "0") int retryCnt) {

        log.warn("⚠️  DLT 수신: {}, retryCnt={}", msg, retryCnt);

        // ----- 재시도 여부 판단 -----
        if (retryCnt >= MAX_RETRY) {
            log.error("❌ 최대 재시도 초과. parking-lot 으로 이동");
            kafkaTemplate.send("message.PARKING_LOT", msg.getChatroomId(), msg);
            return;
        }

        // ----- back‑off (예: 5초) -----
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException ignored) {
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
