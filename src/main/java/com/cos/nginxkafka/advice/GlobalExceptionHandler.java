package com.cos.nginxkafka.advice;

import com.cos.nginxkafka.exception.*;
import com.cos.nginxkafka.util.ApiErrorCodeEnum;
import com.cos.nginxkafka.util.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FailedSendMessageException.class)
    public ResponseEntity<ErrorResponse> failedSendMessage(FailedSendMessageException ex) {
        log.error("[EXCEPTION_MESSAGE] {}", ex.getMessage(), ex);
        return build(ApiErrorCodeEnum.FAILED_SEND_MESSAGE);
    }

    @ExceptionHandler(FailedSaveMessageException.class)
    public ResponseEntity<ErrorResponse> handleFailedSaveMessage(FailedSaveMessageException ex) {
        log.error("[EXCEPTION_MESSAGE] {}", ex.getMessage(), ex);
        return build(ApiErrorCodeEnum.FAILED_SAVE_MESSAGE);
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChatRoomNotFound(ChatRoomNotFoundException ex) {
        log.error("[EXCEPTION_MESSAGE] {}", ex.getMessage(), ex);
        return build(ApiErrorCodeEnum.CHATROOM_NOT_FOUND);
    }

    @ExceptionHandler(KafkaSendFailedException.class)
    public ResponseEntity<ErrorResponse> handleKafkaSendFailed(KafkaSendFailedException ex) {
        log.error("[EXCEPTION_MESSAGE] {}", ex.getMessage(), ex);
        return build(ApiErrorCodeEnum.KAFKA_SEND_FAILED);
    }

    @ExceptionHandler(MongoSaveFailedException.class)
    public ResponseEntity<ErrorResponse> handleMongoSaveFailed(MongoSaveFailedException ex) {
        log.error("[EXCEPTION_MESSAGE] {}", ex.getMessage(), ex);
        return build(ApiErrorCodeEnum.MONGODB_SAVE_FAILED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("[INTERNAL_SERVER_ERROR] {}", ex.getMessage(), ex);
        return build(ApiErrorCodeEnum.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> build(ApiErrorCodeEnum code) {
        return ResponseEntity.status(code.getHttpStatus())
                .body(new ErrorResponse(code));
    }
}
