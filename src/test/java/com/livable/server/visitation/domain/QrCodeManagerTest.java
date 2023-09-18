package com.livable.server.visitation.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livable.server.core.exception.GlobalRuntimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class QrCodeManagerTest {

    @InjectMocks
    QrCodeManager qrCodeManager;

    @Spy
    ObjectMapper objectMapper;

    @DisplayName("QrCodeManager.createQrCode 성공 테스트")
    @Test
    void createQrCodeSuccessTest() throws JsonProcessingException {

        // Given
        // When
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String qrCode = qrCodeManager.createQrCode(startDate, endDate);

        // Then
        assertThat(qrCode).isNotNull();
        then(objectMapper).should(times(1)).writeValueAsString(any());
    }

    @DisplayName("QrCodeManager.createQrCode 실패 테스트_1")
    @Test
    void createQrCodeFailTest_1() throws JsonProcessingException {

        // Given
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);

        // When
        GlobalRuntimeException globalRuntimeException = assertThrows(GlobalRuntimeException.class, () -> qrCodeManager.createQrCode(startDate, endDate));

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.INVALID_PERIOD);
    }
}