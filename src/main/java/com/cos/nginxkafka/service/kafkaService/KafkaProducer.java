package com.cos.nginxkafka.service.kafkaService;

import com.cos.nginxkafka.exception.KafkaSendFailedException;
import com.cos.nginxkafka.util.ApiErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import com.cos.nginxkafka.dto.ChatRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, ChatRequestDTO> kafkaTemplate;

    public void sendMessage(String topic, String content, String senderId, String chatroomId) {
        try {
            ChatRequestDTO message = new ChatRequestDTO().builder()
                    .content(content)
                    .sender(senderId)
                    .chatroomId(chatroomId)
                    .timestamp(LocalDateTime.now())
                    .build();

            String key = "chat";
            log.info("Sent Kafka message(KafkaProducer): {}", message);
            kafkaTemplate.send(topic, key ,message)
                    .whenComplete((msg, throwable) -> {
                        if (throwable != null) {

                            kafkaTemplate.send("message.DLT", key, message);
                            throw new KafkaSendFailedException(ApiErrorCodeEnum.KAFKA_SEND_FAILED);
                        } else {
                            log.info("메시지 전송 성공! offset={}", msg.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending Kafka message", e);
        }
    }
}
