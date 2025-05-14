package com.example.comment.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> apiException(ApiException exception) {
        log.error("ApiException occurred. type={} message{} className={}", exception.getErrorType(),
                exception.getMessage(),
                exception.getClass().getName());
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
    }

}
