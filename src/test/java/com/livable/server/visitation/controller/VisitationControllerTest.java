package com.livable.server.visitation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livable.server.core.exception.ErrorCode;
import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.VisitationRequest;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.visitation.mock.ValidateQrCodeSuccessMockRequest;
import com.livable.server.visitation.service.VisitationFacadeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitationController.class)
class VisitationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private VisitationFacadeService visitationFacadeService;

    @DisplayName("[GET][/api/visitation/qr] - QR을 생성 정상 응답")
    @Test
    void createQrCodeSuccessTest() throws Exception {
        String base64QrCode = "base64QrCode임 ㅋㅋ";

        // given
        given(visitationFacadeService.createQrCode(1L)).willReturn(base64QrCode);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/visitation/qr")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(base64QrCode));

        then(visitationFacadeService).should(times(1)).createQrCode(1L);
    }

    @DisplayName("[GET][/api/visitation/qr] - QR생성 오류_1")
    @Test
    void createQrCodeFailTest_1() throws Exception {

        // given
        String errorMessage = VisitationErrorCode.NOT_FOUND.getMessage();

        given(visitationFacadeService.findInvitationTime(anyLong())).willThrow(new GlobalRuntimeException(VisitationErrorCode.NOT_FOUND));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/visitation/qr")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorMessage));

        then(visitationFacadeService).should(times(1)).findInvitationTime(anyLong());
    }

    @DisplayName("[GET][/api/visitation/qr] - QR생성 오류_2")
    @Test
    void createQrCodeFailTest_2() throws Exception {

        // given
        VisitationResponse.InvitationTimeDto invitationTimeDto = VisitationResponse.InvitationTimeDto.builder()
                .startDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now())
                .endDate(LocalDate.now())
                .build();

        String errorMessage = VisitationErrorCode.INVALID_QR_PERIOD.getMessage();

        given(visitationFacadeService.findInvitationTime(anyLong())).willReturn(invitationTimeDto);
        given(visitationFacadeService.createQrCode(any(LocalDateTime.class), any(LocalDateTime.class))).willThrow(new GlobalRuntimeException(VisitationErrorCode.INVALID_QR_PERIOD));


        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/visitation/qr")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorMessage));

        then(visitationFacadeService).should(times(1)).findInvitationTime(anyLong());
        then(visitationFacadeService).should(times(1)).createQrCode(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @DisplayName("[GET][/api/visitation/qr] - QR생성 오류_3")
    @Test
    void createQrCodeFailTest_3() throws Exception {

        // given
        VisitationResponse.InvitationTimeDto invitationTimeDto = VisitationResponse.InvitationTimeDto.builder()
                .startDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now())
                .endDate(LocalDate.now())
                .build();

        String errorMessage = VisitationErrorCode.IO.getMessage();

        given(visitationFacadeService.findInvitationTime(anyLong())).willReturn(invitationTimeDto);
        given(visitationFacadeService.createQrCode(any(LocalDateTime.class), any(LocalDateTime.class))).willThrow(new GlobalRuntimeException(VisitationErrorCode.IO));


        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/visitation/qr")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorMessage));

        then(visitationFacadeService).should(times(1)).findInvitationTime(anyLong());
        then(visitationFacadeService).should(times(1)).createQrCode(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @DisplayName("[POST][/api/visitation/qr] - QR인증 성공")
    @Test
    void validateQrCodeSuccess() throws Exception {
        // given
        String qr = "qr";
        VisitationRequest.ValidateQrDto validateQrCodeSuccessMockRequest = new ValidateQrCodeSuccessMockRequest(qr);

        willDoNothing().given(visitationFacadeService).validateQrCode(anyString());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/visitation/qr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validateQrCodeSuccessMockRequest))
        );

        // then
        resultActions.andExpect(status().isOk());

        then(visitationFacadeService).should(times(1)).validateQrCode(anyString());
    }

    @DisplayName("[POST][/api/visitation/qr] - QR인증 성공")
    @Test
    void validateQrCodeFail() throws Exception {
        // given
        String qr = "qr";
        ErrorCode errorCode = VisitationErrorCode.INVALID_QR_PERIOD;
        String errorMessage = errorCode.getMessage();
        VisitationRequest.ValidateQrDto validateQrCodeSuccessMockRequest = new ValidateQrCodeSuccessMockRequest(qr);


        willThrow(new GlobalRuntimeException(errorCode)).given(visitationFacadeService).validateQrCode(anyString());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/visitation/qr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validateQrCodeSuccessMockRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorMessage));

        then(visitationFacadeService).should(times(1)).validateQrCode(anyString());
    }
}