package com.livable.server.core.response;

import com.livable.server.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {

    public static <T> ResponseEntity<Success<T>> success(@NonNull HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .build();
    }

    public static <T> ResponseEntity<Success<T>> success(@NonNull T data, @NonNull HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(Success.of(data));
    }

    public static ResponseEntity<Error> error(@NonNull String message, @NonNull HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(Error.of(message));
    }

    public static ResponseEntity<Error> error(@NonNull ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(Error.of(errorCode.getMessage()));
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Error {

        private String message;

        public static Error of(@NonNull String errorMessage) {
            return new Error(errorMessage);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Success<T> {

        private T data;

        public static <T> Success<T> of(@NonNull T data) {
            return new Success<>(data);
        }
    }
}
