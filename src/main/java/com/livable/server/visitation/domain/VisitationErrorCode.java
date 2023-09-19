package com.livable.server.visitation.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum VisitationErrorCode implements ErrorCode {

    OBJECTMAPPER(HttpStatus.INTERNAL_SERVER_ERROR, "직렬화 과정에서 알 수 없는 에러가 발생했습니다."),
    QR_ENCODE(HttpStatus.INTERNAL_SERVER_ERROR, "QR코드 생성 중 알 수 없는 에러가 발생했습니다."),
    QR_DECODE(HttpStatus.INTERNAL_SERVER_ERROR, "QR코드를 푸는 과정에서 알 수 없는 에러가 발생했습니다."),
    IO(HttpStatus.INTERNAL_SERVER_ERROR, "I/O를 진행하는 과정에서 알 수 없는 에러가 발생했습니다."),
    INVALID_QR_PERIOD(HttpStatus.BAD_REQUEST, "QR을 생성할 수 없는 시간입니다."),
    INVALID_PERIOD(HttpStatus.BAD_REQUEST, "시작 및 종료시간이 올바르지 않습니다."),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 정보입니다."),
    ALREADY_REGISTER_PARKING(HttpStatus.BAD_REQUEST, "이미 주차 등록을 완료하였습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
