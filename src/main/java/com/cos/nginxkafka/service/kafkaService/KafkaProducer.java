package com.cos.nginxkafka.service.kafkaService;

import com.cos.nginxkafka.exception.KafkaSendToDLTException;
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
        ChatRequestDTO message = ChatRequestDTO.builder()
                .content(content)
                .sender(senderId)
                .chatroomId(chatroomId)
                .timestamp(LocalDateTime.now())
                .build();

        String key = "chat";
        log.info("Sent Kafka message(KafkaProducer): {}", message);

        try {
            // send() 는 비동기로 future 를 돌려주므로, get() 으로 블로킹하여
            // 실제 브로커 전송 성공/실패를 확인한다.
            kafkaTemplate.send(topic, key, message)
                         .get(5, java.util.concurrent.TimeUnit.SECONDS);

            log.info("메시지 전송 성공!");
        } catch (Exception ex) {

            // 브로커 다운 또는 타임아웃 → DLT 로 우선 재전송
            kafkaTemplate.send("message.DLT", key, message);

            log.info("메시지 전송 실패!");
            throw new KafkaSendToDLTException(ApiErrorCodeEnum.KAFKA_SEND_TO_DLT);
        }
    }
}
