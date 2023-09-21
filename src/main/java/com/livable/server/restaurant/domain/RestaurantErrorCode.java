package com.livable.server.restaurant.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RestaurantErrorCode implements ErrorCode {
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "존재하지 않는 식당 종류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
