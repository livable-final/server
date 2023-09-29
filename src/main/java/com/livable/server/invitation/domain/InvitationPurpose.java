package com.livable.server.invitation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum InvitationPurpose {

    MEETING("meeting"),
    INTERVIEW("interview"),
    PERIOD_WORK("fixedTermWork"),
    SEMINAR("seminar"),
    AFTER_SERVICE("as"),
    ETC("etc");

    private final String value;
}
