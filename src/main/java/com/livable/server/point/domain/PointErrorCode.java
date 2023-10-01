package com.livable.server.point.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PointErrorCode implements ErrorCode {

    POINT_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 포인트 정보입니다."),
    POINT_NOT_EXIST_FOR_CURRENT_MONTH(HttpStatus.BAD_REQUEST, "현재 달에 지급된 리뷰 포인트가 존재하지 않습니다."),
    ACHIEVEMENT_POINT_PAID_FAILED(HttpStatus.BAD_REQUEST, "목표달성 포인트는 당일에만 지급받을 수 있습니다."),
    ACHIEVEMENT_POINT_PAID_ALREADY(HttpStatus.BAD_REQUEST, "금일 목표달성 포인트를 이미 지급 받았습니다."),
    ACHIEVEMENT_POINT_NOT_MATCHED(HttpStatus.BAD_REQUEST, "목표달성 포인트를 지급 받을 수 있는 리뷰 개수가 부족합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}