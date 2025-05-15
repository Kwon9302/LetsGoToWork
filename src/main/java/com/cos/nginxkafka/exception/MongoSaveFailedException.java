package com.cos.nginxkafka.exception;

import com.cos.nginxkafka.util.ApiErrorCodeEnum;

public class MongoSaveFailedException extends RuntimeException {
    public MongoSaveFailedException(ApiErrorCodeEnum e) {
        super(e.getMessage());
    }
}
