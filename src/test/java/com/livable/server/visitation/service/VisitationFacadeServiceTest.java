package com.livable.server.visitation.service;

import com.livable.server.invitation.service.InvitationService;
import com.livable.server.visitation.dto.VisitationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class VisitationFacadeServiceTest {

    private final static String QR_CODE = "QR_CODE";

    @InjectMocks
    VisitationFacadeService visitationFacadeService;

    @Mock
    VisitationService visitationService;
    @Mock
    InvitationService invitationService;
    @Mock
    VisitorService visitorService;

    @DisplayName("VisitationFacadeService.createQrCode 성공 테스트")
    @Test
    void createQrCodeSuccessTest() {
        // Given
        VisitationResponse.InvitationTimeDto invitationTimeDto = VisitationResponse.InvitationTimeDto.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now())
                .build();

        given(visitorService.findInvitationId(anyLong())).willReturn(1L);
        given(invitationService.findInvitationTime(anyLong())).willReturn(invitationTimeDto);
        given(visitationService.createQrCode(any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(QR_CODE);

        // When
        String qrCode = visitationFacadeService.createQrCode(1L);

        // Then
        assertThat(qrCode).isEqualTo(QR_CODE);
        then(visitorService).should(times(1)).findInvitationId(anyLong());
        then(invitationService).should(times(1)).findInvitationTime(anyLong());
        then(visitationService).should(times(1)).createQrCode(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @DisplayName("VisitationFacadeService.validateQrCode 성공 테스트")
    @Test
    void validateQrCodeSuccessTest() {

        // Given
        willDoNothing().given(visitationService).validateQrCode(anyString());

        // When
        visitationFacadeService.validateQrCode(QR_CODE);

        // Then
        then(visitationService).should(times(1)).validateQrCode(anyString());
    }
}