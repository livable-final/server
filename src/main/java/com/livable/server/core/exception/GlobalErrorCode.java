package com.livable.server.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    INVALID_TYPE(HttpStatus.BAD_REQUEST, "입력 형식이 올바르지 않습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
