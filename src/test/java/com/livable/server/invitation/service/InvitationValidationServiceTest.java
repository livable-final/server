package com.livable.server.invitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.member.domain.MemberErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class InvitationValidationServiceTest {

    @Mock
    JwtTokenProvider tokenProvider;

    @InjectMocks
    InvitationValidationService invitationValidationService;

    @DisplayName("[실패] 방문자 callback - 잘못된 토큰으로 요청한 경우")
    @Test
    void validateVisitorFail_01() {
        // Given
        String token = "token";
        given(tokenProvider.parseClaims(anyString()))
                .willThrow(new GlobalRuntimeException(MemberErrorCode.INVALID_TOKEN));

        // When
        GlobalRuntimeException exception = assertThrows(GlobalRuntimeException.class,
                () -> invitationValidationService.validateVisitor(token));

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.INVALID_TOKEN);
    }
}