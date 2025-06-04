package com.cos.nginxkafka.advice;

import com.cos.nginxkafka.exception.KafkaDLTFailedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaErrorHandlerConfig {
    @Bean
    public DefaultErrorHandler dltErrorHandler() {

        DefaultErrorHandler handler = new DefaultErrorHandler(
                KafkaErrorHandlerConfig::recoverDltFailure,
                new FixedBackOff(0, 0)      // 재시도 0회(즉시 실패)
        );

        handler.addNotRetryableExceptions(KafkaDLTFailedException.class);

        return handler;
    }

    /**  DLT 재처리 실패 시 실행되는 공통 로깅 로직  */
    private static void recoverDltFailure(ConsumerRecord<?, ?> record, Exception ex) {
        log.error("❌ DLT 처리 실패 – topic={}, partition={}, offset={}, key={}, ex={}",
                record.topic(), record.partition(), record.offset(), record.key(), ex.toString());

    }
}
