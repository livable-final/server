package com.livable.server.member.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 회원 정보입니다."),

    BUILDING_INFO_NOT_EXIST(HttpStatus.BAD_REQUEST, "해당 회원의 빌딩 정보가 존재하지 않습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰 정보입니다."),

    INVALID_ACTOR_TYPE(HttpStatus.BAD_REQUEST, "존재하지 않는 유형의 사용자입니다."),
  
    RETRIEVE_ACCESSCARD_FAILED(HttpStatus.BAD_REQUEST, "출입 카드 정보를 조회 할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
