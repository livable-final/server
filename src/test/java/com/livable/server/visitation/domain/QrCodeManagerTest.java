package com.livable.server.visitation.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.visitation.mock.MockQrPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class QrCodeManagerTest {

    @InjectMocks
    QrCodeManager qrCodeManager;

    @Mock
    QrCodeEncoder qrCodeEncoder;

    @Mock
    QrCodeDecoder qrCodeDecoder;

    @DisplayName("QrCodeManager.createQrCode 성공 테스트")
    @Test
    void createQrCodeSuccessTest() {

        // Given
        String qrCode = "QR_CODE";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        BufferedImage bufferedImage = new BufferedImage(10, 10, 1);
        given(qrCodeEncoder.createQrCodeBufferdImage(any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(bufferedImage);
        given(qrCodeEncoder.encodeQrcodeToBase64(any(BufferedImage.class))).willReturn(qrCode);

        // When
        String result = qrCodeManager.createQrCode(startDate, endDate);

        // Then
        assertThat(result).isEqualTo(qrCode);
        then(qrCodeEncoder).should(times(1)).createQrCodeBufferdImage(any(LocalDateTime.class), any(LocalDateTime.class));
        then(qrCodeEncoder).should(times(1)).encodeQrcodeToBase64(any(BufferedImage.class));
    }

    @DisplayName("QrCodeManager.createQrCode 실패 테스트_1")
    @Test
    void createQrCodeFailTest_1() {

        // Given
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);

        // When
        GlobalRuntimeException globalRuntimeException = assertThrows(GlobalRuntimeException.class, () -> qrCodeManager.createQrCode(startDate, endDate));

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.INVALID_PERIOD);
    }

    @DisplayName("QrCodeManager.createQrCode 실패 테스트_2")
    @Test
    void createQrCodeFailTest_2() {

        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);

        // When
        GlobalRuntimeException globalRuntimeException = assertThrows(GlobalRuntimeException.class, () -> qrCodeManager.createQrCode(startDate, endDate));

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.INVALID_QR_PERIOD);
    }

    @DisplayName("QrCodeManager.validateQrCode 성공 테스트")
    @Test
    void validateQrCodeSuccessTest() {

        // Given
        String qrCode = "QR_CODE";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        QrPayload qrPayload = new MockQrPayload(startDate, endDate);
        given(qrCodeDecoder.getQrPayload(anyString())).willReturn(qrPayload);

        // When
        qrCodeManager.validateQrCode(qrCode);

        // Then
        then(qrCodeDecoder).should(times(1)).getQrPayload(anyString());
    }

    @DisplayName("QrCodeManager.validateQrCode 실패 테스트_1")
    @Test
    void validateQrCodeFailTest_1() {

        // Given
        String qrCode = "QR_CODE";
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        QrPayload qrPayload = new MockQrPayload(startDate, endDate);
        given(qrCodeDecoder.getQrPayload(anyString())).willReturn(qrPayload);

        // When
        GlobalRuntimeException globalRuntimeException =
                assertThrows(GlobalRuntimeException.class, () -> qrCodeManager.validateQrCode(qrCode));

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.INVALID_PERIOD);
        then(qrCodeDecoder).should(times(1)).getQrPayload(anyString());
    }

    @DisplayName("QrCodeManager.validateQrCode 실패 테스트_2")
    @Test
    void validateQrCodeFailTest_2() {

        // Given
        String qrCode = "QR_CODE";
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        QrPayload qrPayload = new MockQrPayload(startDate, endDate);
        given(qrCodeDecoder.getQrPayload(anyString())).willReturn(qrPayload);

        // When
        GlobalRuntimeException globalRuntimeException =
                assertThrows(GlobalRuntimeException.class, () -> qrCodeManager.validateQrCode(qrCode));

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.INVALID_QR_PERIOD);
        then(qrCodeDecoder).should(times(1)).getQrPayload(anyString());
    }

    @DisplayName("QrCodeManager.validateQrCode 실패 테스트_3")
    @Test
    void validateQrCodeFailTest_3() {

        // Given
        String qrCode = "QR_CODE";
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        QrPayload qrPayload = new MockQrPayload(startDate, endDate);
        given(qrCodeDecoder.getQrPayload(anyString())).willReturn(qrPayload);

        // When
        GlobalRuntimeException globalRuntimeException =
                assertThrows(GlobalRuntimeException.class, () -> qrCodeManager.validateQrCode(qrCode));

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.INVALID_QR_PERIOD);
        then(qrCodeDecoder).should(times(1)).getQrPayload(anyString());
    }
}