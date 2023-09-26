package com.livable.server.visitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.ParkingLog;
import com.livable.server.entity.Visitor;
import com.livable.server.invitation.service.InvitationService;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.visitation.mock.MockDetailInformationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    ParkingLogService parkingLogService;

    @DisplayName("VisitationFacadeService.findVisitationDetailInformation 성공 테스트")
    @Test
    void findVisitationDetailInformationSuccessTest() {
        // Given
        MockDetailInformationDto mockDetailInformationDto = new MockDetailInformationDto();
        given(visitorService.findVisitationDetailInformation(anyLong())).willReturn(mockDetailInformationDto);

        // When
        VisitationResponse.DetailInformationDto detailInformationDto =
                visitationFacadeService.findVisitationDetailInformation(1L);

        // Then
        assertThat(detailInformationDto).isEqualTo(mockDetailInformationDto);
        then(visitorService).should(times(1)).findVisitationDetailInformation(anyLong());
    }

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

        given(invitationService.findInvitationTime(anyLong())).willReturn(invitationTimeDto);
        given(visitationService.createQrCode(any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(QR_CODE);

        // When
        String qrCode = visitationFacadeService.createQrCode(1L);

        // Then
        assertThat(qrCode).isEqualTo(QR_CODE);
        then(invitationService).should(times(1)).findInvitationTime(anyLong());
        then(visitationService).should(times(1)).createQrCode(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @DisplayName("VisitationFacadeService.validateQrCode 성공 테스트")
    @Test
    void validateQrCodeSuccessTest() {

        // Given
        willDoNothing().given(visitationService).validateQrCode(anyString());

        // When
        visitationFacadeService.validateQrCode(QR_CODE, 1L);

        // Then
        then(visitationService).should(times(1)).validateQrCode(anyString());
    }

    @DisplayName("VisitationFacadeService.registerParking 성공 테스트")
    @Test
    void registerParkingSuccessTest() {
        // Given
        Visitor visitor = Visitor.builder()
                .id(1L)
                .build();
        given(visitorService.findById(anyLong())).willReturn(visitor);
        given(parkingLogService.findParkingLogByVisitorId(any())).willReturn(Optional.empty());
        willDoNothing().given(parkingLogService).registerParkingLog(any(), anyString());

        // When
        visitationFacadeService.registerParking(visitor.getId(), "12가1234");

        // Then
        then(visitorService).should(times(1)).findById(anyLong());
        then(parkingLogService).should(times(1)).findParkingLogByVisitorId(any());
        then(parkingLogService).should(times(1)).registerParkingLog(any(), anyString());
    }

    @DisplayName("VisitationFacadeService.registerParking 실패 테스트")
    @Test
    void registerParkingFailTest() {
        // Given
        ParkingLog parkingLog = ParkingLog.builder()
                .build();
        given(parkingLogService.findParkingLogByVisitorId(any())).willReturn(Optional.of(parkingLog));

        // When
        GlobalRuntimeException globalRuntimeException = assertThrows(
                GlobalRuntimeException.class,
                () -> visitationFacadeService.registerParking(any(), "12가1234")
        );

        // Then
        then(parkingLogService).should(times(1)).findParkingLogByVisitorId(any());
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.ALREADY_REGISTER_PARKING);
    }
}