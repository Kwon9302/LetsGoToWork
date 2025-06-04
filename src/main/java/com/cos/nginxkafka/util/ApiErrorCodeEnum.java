package com.cos.nginxkafka.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiErrorCodeEnum {
    FAILED_SEND_MESSAGE(HttpStatus.CONFLICT,"0000","메시지가 전송되지 않았습니다."),
    FAILED_SAVE_MESSAGE(HttpStatus.CONFLICT,"0001","메시지가 저장되지 않았습니다."),
    KAFKA_SEND_TO_DLT(HttpStatus.SERVICE_UNAVAILABLE, "0100", "Kafka 전송에 실패하여 DLT로 전송합니다."),
    KAFKA_DLT_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "0100", "DLT 메시지 처리 실패."),
    MONGODB_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "0101", "MongoDB 저장 중 오류가 발생했습니다."),
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "0102", "요청한 채팅방을 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "0103", "잘못된 요청입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"999","SERVER ERROR 입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
