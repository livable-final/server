package com.livable.server.invitation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum InvitationPurpose {

    MEETING("회의"),
    INTERVIEW("면접"),
    PERIOD_WORK("기간 근무"),
    SEMINAR("세미나"),
    AFTER_SERVICE("AS/점검"),
    ETC("기타");

    private final String value;
}
