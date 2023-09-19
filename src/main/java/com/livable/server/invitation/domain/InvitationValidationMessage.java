package com.livable.server.invitation.domain;

public interface InvitationValidationMessage {
    String REQUIRED_FUTURE_DATE = "선택된 시간이 현재보다 과거일 수 없습니다.";
    String REQUIRED_VISITOR_COUNT = "방문자는 최소 1명 이상 30명 이하여야 합니다. (면접은 1명)";
    String NOT_NULL = "값이 Null 일수 없습니다.";
}
