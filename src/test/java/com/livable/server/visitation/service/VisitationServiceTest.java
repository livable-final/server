package com.livable.server.visitation.service;

import com.livable.server.visitation.domain.QrCodeManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class VisitationServiceTest {

    @InjectMocks
    VisitationService visitationService;

    @Mock
    QrCodeManager qrCodeManager;

    @DisplayName("VisitationService.createQrCode 성공 테스트")
    @Test
    void createQrCodeSuccessTest() {
        // Given
        String qrCode = "qrCode";
        LocalDateTime startDate = LocalDateTime.of(2023, 9, 18, 1, 10);
        LocalDateTime endDate = LocalDateTime.of(2023, 9, 18, 1, 11);

        given(qrCodeManager.createQrCode(any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(qrCode);

//         When
        String resultQrCode = visitationService.createQrCode(startDate, endDate);

        // Then
        assertThat(qrCode).isEqualTo(resultQrCode);
        then(qrCodeManager).should(times(1)).createQrCode(any(LocalDateTime.class), any(LocalDateTime.class));
    }
}