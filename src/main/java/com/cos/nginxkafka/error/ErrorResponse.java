package com.cos.nginxkafka.error;

public record ErrorResponse(String resultCode, String resultMessage) {
    public ErrorResponse(ApiErrorCode ec) {
        this(ec.getCode(), ec.getMessage());
    }
}
