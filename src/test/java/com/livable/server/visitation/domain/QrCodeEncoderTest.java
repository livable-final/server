package com.livable.server.visitation.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class QrCodeEncoderTest {
    
    @InjectMocks
    QrCodeEncoder qrCodeEncoder;

    @Spy
    ObjectMapper objectMapper;

    @DisplayName("QrCodeEncoder.createQrCodeBufferdImage 성공 테스트")
    @Test
    void createQrCodeBufferdImageSuceessTest() throws JsonProcessingException {
        // given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        BufferedImage bufferedImage = new BufferedImage(10, 10, 10);
        MockedStatic<MatrixToImageWriter> imageWriter = mockStatic(MatrixToImageWriter.class);
        given(MatrixToImageWriter.toBufferedImage(any(BitMatrix.class))).willReturn(bufferedImage);

        // when
        BufferedImage qrCodeBufferdImage = qrCodeEncoder.createQrCodeBufferdImage(startDate, endDate);

        // then
        assertThat(qrCodeBufferdImage).isEqualTo(bufferedImage);
        then(objectMapper).should(times(1)).registerModule(any(JavaTimeModule.class));
        then(objectMapper).should(times(1)).writeValueAsString(any(HashMap.class));

        imageWriter.close();
    }

    @DisplayName("QrCodeEncoder.createQrCodeBufferdImage 실패 테스트")
    @Test
    void createQrCodeBufferdImageFailTest() throws JsonProcessingException {
        // given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        given(objectMapper.writeValueAsString(any(HashMap.class))).willThrow(JsonProcessingException.class);

        // when
        RuntimeException runtimeException =
                assertThrows(RuntimeException.class, () -> qrCodeEncoder.createQrCodeBufferdImage(startDate, endDate));

        // then
        assertThat(runtimeException.getStackTrace()).isNotNull();
        then(objectMapper).should(times(1)).registerModule(any(JavaTimeModule.class));
        then(objectMapper).should(times(1)).writeValueAsString(any(HashMap.class));
    }

}