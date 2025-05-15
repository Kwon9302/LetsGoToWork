package com.cos.nginxkafka.exception;

import com.cos.nginxkafka.util.ApiErrorCodeEnum;

public class KafkaSendFailedException extends RuntimeException {
    public KafkaSendFailedException(ApiErrorCodeEnum e) {
        super(e.getMessage());
    }
}
