package com.livable.server.visitation.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.livable.server.core.exception.GlobalRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class QrCodeEncoder {

    private static final int DEFAULT_WIDTH = 170;
    private static final int DEFAULT_HEIGHT = 170;
    private static final String EXPIRATION_START_DATE_KEY = "startDate";
    private static final String EXPIRATION_END_DATE_KEY = "endDate";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_FORMAT = "png";

    private final ObjectMapper objectMapper;

    public BufferedImage createQrCodeBufferdImage(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        try {

            HashMap<String, LocalDateTime> expirationPeriodMap = getExpirationPeriodMap(startDateTime, endDateTime);
            String contents = objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(expirationPeriodMap);

            Map<EncodeHintType, Object> encodeHints = getEncodeHints();

            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, DEFAULT_WIDTH, DEFAULT_HEIGHT, encodeHints);

            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (JsonProcessingException | WriterException e) {
            throw new RuntimeException(e);
        }
    }

    private HashMap<String, LocalDateTime> getExpirationPeriodMap(final LocalDateTime startDate, final LocalDateTime endDate) {
        return new HashMap<>() {{
            put(EXPIRATION_START_DATE_KEY, startDate);
            put(EXPIRATION_END_DATE_KEY, endDate);
        }};
    }

    private Map<EncodeHintType, Object> getEncodeHints() {
        return new EnumMap<>(EncodeHintType.class) {{
            put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
        }};
    }

    public String encodeQrcodeToBase64(final BufferedImage bufferedImage) {
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ImageIO.write(bufferedImage, DEFAULT_FORMAT, outputStream);

            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (IOException e) {
            log.error("QrCodeManager.encodeQrcodeToBase64", e);
            throw new GlobalRuntimeException(VisitationErrorCode.IO);
        }
    }
}
