package com.livable.server.visitation.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.visitation.mock.MockQrPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QrCodeDecoderTest {

    private static String qrCode;

    @InjectMocks
    QrCodeDecoder qrCodeDecoder;

    @Mock
    ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        QrCodeManager qrCodeManager = new QrCodeManager(
                new QrCodeEncoder(new ObjectMapper()), new QrCodeDecoder(new ObjectMapper())
        );
        qrCode = qrCodeManager.createQrCode(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
    }

    @DisplayName("QrCodeDecoder.getQrPayload 성공 테스트")
    @Test
    void getQrPayloadSuccessTest() throws JsonProcessingException {

        // given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        QrPayload qrPayload = new MockQrPayload(startDate, endDate);
        given(objectMapper.readValue(anyString(), any(Class.class))).willReturn(qrPayload);

        // when
        QrPayload result = qrCodeDecoder.getQrPayload(qrCode);


        // then
        assertThat(result).isEqualTo(qrPayload);
    }

    @DisplayName("QrCodeDecoder.getQrPayload 실패 테스트")
    @Test
    void getQrPayloadFailTest() throws JsonProcessingException {

        // given
        given(objectMapper.readValue(anyString(), any(Class.class))).willThrow(JsonProcessingException.class);

        // when
        GlobalRuntimeException globalRuntimeException =
                assertThrows(GlobalRuntimeException.class, () -> qrCodeDecoder.getQrPayload(qrCode));


        // then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.OBJECTMAPPER);
    }
}