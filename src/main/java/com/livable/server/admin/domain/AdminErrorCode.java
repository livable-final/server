package com.livable.server.admin.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AdminErrorCode implements ErrorCode {
    NOT_EXIST_ADMIN(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자 입니다."),
    INVALID_QUERY(HttpStatus.BAD_REQUEST, "검색 조건이 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
