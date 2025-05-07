package com.cos.nginxkafka.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiErrorCode {
    DUPLICATED_ID(HttpStatus.CONFLICT, "0001", "동일한 아이디가 존재합니다."),
    NONEXISTENT_CHATROOM(HttpStatus.NOT_FOUND, "0019", "채팅방이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
