package com.livable.server.visitation.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
}