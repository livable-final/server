package com.livable.server.core.exception;

import com.livable.server.core.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse.Error> methodMethodArgumentTypeMismatchExceptionHandle(MethodArgumentTypeMismatchException e) {
        return ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse.Error> bindException(BindException e) {
        log.error("bindException", e);

        return ApiResponse.error(e.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GlobalRuntimeException.class)
    public ResponseEntity<ApiResponse.Error> globalRuntimeExceptionHandle(GlobalRuntimeException e) {
        log.error("globalRuntimeExceptionHandle", e);

        return ApiResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse.Error> runtimeExceptionHandle(RuntimeException e) {
        log.error("runtimeExceptionHandle", e);

        return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse.Error> exceptionHandle(Exception e) {
        log.error("exceptionHandle", e);

        return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
