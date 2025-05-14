package com.example.comment.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ErrorType errorType;
    private final String message;
}
