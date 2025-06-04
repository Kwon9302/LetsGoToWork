package com.cos.nginxkafka.exception;

import com.cos.nginxkafka.util.ApiErrorCodeEnum;

public class KafkaSendToDLTException extends RuntimeException {
    public KafkaSendToDLTException(ApiErrorCodeEnum e) {
        super(e.getMessage());
    }
}
