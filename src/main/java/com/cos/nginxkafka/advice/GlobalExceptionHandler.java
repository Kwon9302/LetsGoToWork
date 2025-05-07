package com.cos.nginxkafka.advice;

import com.cos.nginxkafka.error.ErrorResponse;
import com.cos.nginxkafka.error.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public Object handleGlobal(GlobalException ex, NativeWebRequest req) {
        log.error("[{}] {}", ex.getErrorCode().name(), ex.getMessage(), ex);

        if (req.getHeader("sec-websocket-key") != null) return null; // WS 는 개별 advice 처리
        if (req.getNativeRequest() instanceof jakarta.servlet.http.HttpServletRequest) {
            return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                    .body(new ErrorResponse(ex.getErrorCode()));
        }

        throw ex;
    }
}
