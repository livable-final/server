package com.livable.server.core.exception;

import lombok.Getter;

@Getter
public class GlobalRuntimeException extends RuntimeException {

    private final ErrorCode errorCode;

    public GlobalRuntimeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
