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

    BAD_PAGING_REQUEST(HttpStatus.BAD_REQUEST, "페이징 정보가 잘못 되었습니다."),

    MENU_RANK_NOT_FOUND(HttpStatus.BAD_REQUEST, "메뉴 랭킹을 불러 올 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
