package com.cos.nginxkafka.exception;

import com.cos.nginxkafka.util.ApiErrorCodeEnum;

public class ChatRoomNotFoundException extends RuntimeException {
    public ChatRoomNotFoundException(ApiErrorCodeEnum e) {
        super(e.getMessage());
    }
}
