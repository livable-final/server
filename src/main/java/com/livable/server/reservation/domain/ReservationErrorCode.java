package com.livable.server.reservation.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "날짜 범위가 올바르지 않습니다");

    private final HttpStatus httpStatus;
    private final String message;
    }
