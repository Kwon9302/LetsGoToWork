package com.cos.nginxkafka.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ApiException;


public class GlobalException extends RuntimeException {
    private final ApiErrorCode apiErrorCode;

    GlobalException(ApiErrorCode apiErrorCode) {
        super(apiErrorCode.getMessage());
        this.apiErrorCode = apiErrorCode;
    }

    public GlobalException(ApiErrorCode apiErrorCode, String message) {
        super(message);
        this.apiErrorCode = apiErrorCode;
    }

    public ApiErrorCode getErrorCode() { return apiErrorCode; }

}
