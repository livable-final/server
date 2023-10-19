package com.livable.server.invitation.controller;

import com.livable.server.core.util.ActorType;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.TestConfig;
import com.livable.server.invitation.service.InvitationValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(InvitationValidationController.class)
class InvitationValidationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider tokenProvider;

    @MockBean
    InvitationValidationService invitationValidationService;

    @DisplayName("[성공] 방문자 Callback - 정상 응답")
    @Test
    void validateVisitorSuccess_01() throws Exception {
        // Given
        String token = tokenProvider.createActorToken(ActorType.VISITOR, 1L, new Date(Long.MAX_VALUE));

        // When
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/invitation/callback")
                                .param("token", token)
                );

        // Then
        resultActions
                .andExpect(status().isFound())
                .andExpect(header().string("Authorization", "Bearer " + token));
    }
}