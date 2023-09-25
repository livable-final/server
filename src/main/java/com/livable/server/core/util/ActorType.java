package com.livable.server.core.util;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.member.domain.MemberErrorCode;

import java.util.Arrays;

public enum ActorType {
    MEMBER, VISITOR;

    public static ActorType of(String type) {
        return Arrays.stream(values())
                .filter(actorType -> actorType.name().equals(type))
                .findFirst()
                .orElseThrow(() -> new GlobalRuntimeException(MemberErrorCode.INVALID_ACTOR_TYPE));
    }
}

