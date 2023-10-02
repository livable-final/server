package com.livable.server.menu.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MenuErrorCode implements ErrorCode {

    RETRIEVE_ROULETTE_MENU_FAILED(HttpStatus.BAD_REQUEST, "룰렛 메뉴 정보를 불러 올 수 없습니다."),

    MENU_NOT_EXIST(HttpStatus.BAD_REQUEST, "메뉴를 찾을 수 없습니다."),

    BUILDING_NOT_VALID(HttpStatus.BAD_REQUEST, "빌딩 정보가 일치 하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
