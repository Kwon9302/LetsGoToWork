package com.cos.nginxkafka.util;

public record ErrorResponse(String resultCode, String resultMessage) {
    public ErrorResponse(ApiErrorCodeEnum e) {
        this(e.getCode(), e.getMessage());
    }
}
