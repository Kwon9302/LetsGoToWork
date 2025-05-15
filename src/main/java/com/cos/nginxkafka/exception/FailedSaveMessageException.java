package com.cos.nginxkafka.exception;

import com.cos.nginxkafka.util.ApiErrorCodeEnum;

public class FailedSaveMessageException extends RuntimeException {
    public FailedSaveMessageException(ApiErrorCodeEnum e) {
        super(e.getMessage());
    }
}
