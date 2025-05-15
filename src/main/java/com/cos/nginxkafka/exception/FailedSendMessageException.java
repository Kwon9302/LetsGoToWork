package com.cos.nginxkafka.exception;

import com.cos.nginxkafka.util.ApiErrorCodeEnum;

public class FailedSendMessageException extends RuntimeException {
    public FailedSendMessageException(ApiErrorCodeEnum e) {
        super(e.getMessage());
    }
}
