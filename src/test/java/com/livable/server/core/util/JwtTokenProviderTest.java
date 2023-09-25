package com.livable.server.core.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void init() {
        String testSecretKey = "di0xNUNaQDR1MWksaXM4MH5rdSZYLEM2I3dbR0ZQcWJUOVl5UFhmOV52cEROLmE0bCZheHdWLztCZHJoVjwz";
        tokenProvider = new JwtTokenProvider(testSecretKey);
    }

    @DisplayName("[성공] Member 토큰 검증")
    @Test
    void validateMemberToken() {
        // Given
        Date expireDate = new Date(new Date().getTime() + 1000000);
        String memberToken = tokenProvider.createActorToken(ActorType.MEMBER, 1L, expireDate);

        // When
        boolean isValidToken = tokenProvider.isValidateToken(memberToken);

        // Then
        assertThat(isValidToken).isTrue();
    }

    @DisplayName("[성공] Visitor 토큰 검증")
    @Test
    void validateVisitorToken() {
        // Given
        Date expireDate = new Date(new Date().getTime() + 1000000);
        String visitorToken = tokenProvider.createActorToken(ActorType.VISITOR, 1L, expireDate);

        // When
        boolean isValidToken = tokenProvider.isValidateToken(visitorToken);

        // Then
        assertThat(isValidToken).isTrue();
    }
}