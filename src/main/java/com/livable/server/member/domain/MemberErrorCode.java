package com.livable.server.member.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 회원 정보입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
