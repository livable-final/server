package com.livable.server.core.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getMessage();

    HttpStatus getHttpStatus();
}
