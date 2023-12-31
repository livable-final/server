package com.livable.server.invitation.domain;

import com.livable.server.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum InvitationErrorCode implements ErrorCode {
    MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 회원 정보입니다."),
    INVITATION_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 초대장 정보입니다."),
    INVALID_INTERVIEW_MAXIMUM_NUMBER(HttpStatus.BAD_REQUEST, "면접 초대 가능 인원수는 1명입니다."),
    INVALID_INVITATION_MAXIMUM_NUMBER(HttpStatus.BAD_REQUEST, "한 초대장에 등록할 수 있는 최대 방문자는 30명입니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "종료 날짜가 시작 날짜보다 과거일 수 없습니다."),
    INVALID_TIME(HttpStatus.BAD_REQUEST, "종료 시간이 시작 시간보다 과거일 수 없습니다."),
    INVALID_TIME_UNIT(HttpStatus.BAD_REQUEST, "시간의 분 단위는 0분 또는 30분이어야 합니다."),
    INVALID_RESERVATION_COUNT(HttpStatus.BAD_REQUEST, "해당 날짜 또는 시간에 예약된 장소 정보가 없습니다."),
    INVALID_INVITATION_OWNER(HttpStatus.FORBIDDEN, "초대장을 작성한 회원만 접근이 가능합니다."),
    INVALID_DELETE_DATE(HttpStatus.BAD_REQUEST, "초대장 삭제는 방문일 기준 전날까지만 가능합니다."),
    CAN_NOT_CHANGED_COMMON_PLACE_OF_INVITATION(HttpStatus.BAD_REQUEST, "초대장의 예약된 장소는 변경할 수 없습니다.");

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
