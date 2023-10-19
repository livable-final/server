package com.livable.server.review.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReviewErrorCode implements ErrorCode {
    MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 회원 정보입니다."),
    RESTAURANT_NOT_EXITST(HttpStatus.BAD_REQUEST, "존재하지 않는 음식점 정보입니다."),
    MENUS_NOT_CHOICE(HttpStatus.BAD_REQUEST, "하나 이상의 메뉴를 선택해 주세요."),
    ALREADY_HAVE_A_REVIEW(HttpStatus.BAD_REQUEST, "리뷰는 하루에 한 개만 작성이 가능합니다.");


    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
