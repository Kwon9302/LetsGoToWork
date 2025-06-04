package com.cos.nginxkafka.exception;

import com.cos.nginxkafka.util.ApiErrorCodeEnum;

public class KafkaDLTFailedException extends RuntimeException {
    public KafkaDLTFailedException(ApiErrorCodeEnum e) {
        super(e.getMessage());
    }
}
