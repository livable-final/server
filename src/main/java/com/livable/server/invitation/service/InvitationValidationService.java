package com.livable.server.invitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.member.domain.MemberErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InvitationValidationService {

    private final JwtTokenProvider tokenProvider;

    public void validateVisitor(String token) {
        Claims claims = tokenProvider.parseClaims(token);

        String actorType = claims.get("actorType", String.class);
        if (!actorType.equals(ActorType.VISITOR.name())) {
            throw new GlobalRuntimeException(MemberErrorCode.INVALID_TOKEN);
        }
    }
}
