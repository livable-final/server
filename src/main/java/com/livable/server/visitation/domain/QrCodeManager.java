package com.livable.server.visitation.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.livable.server.core.exception.GlobalRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
public class QrCodeManager {

    private static final int DEFAULT_WIDTH = 170;
    private static final int DEFAULT_HEIGHT = 170;
    private static final String DEFAULT_FORMAT = "png";
    private static final int DEFAULT_QR_CODE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_BACKGROUND_COLOR = 0xFF2563EA;
    private static final String EXPIRATION_START_DATE_KEY = "startDate";
    private static final String EXPIRATION_END_DATE_KEY = "endDate";
    private static final String DEFAULT_CHARSET = "UTF-8";

    private final ObjectMapper objectMapper;

    public String createQrCode(LocalDateTime startDate, LocalDateTime endDate) {

        validatePeriod(startDate, endDate);

        BufferedImage qrCode = createQrCodeImage(startDate, endDate);

        return encodeQrcodeToBase64(qrCode);
    }

    private void validatePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
            throw new GlobalRuntimeException(VisitationErrorCode.INVALID_PERIOD);
        }

        if (!(startDate.isBefore(LocalDateTime.now()) && endDate.isAfter(LocalDateTime.now()))) {
            throw new GlobalRuntimeException(VisitationErrorCode.INVALID_QR_PERIOD);
        }
    }

    private BufferedImage createQrCodeImage(LocalDateTime startDateTime, LocalDateTime endDateTime) {
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

    private HashMap<String, LocalDateTime> getExpirationPeriodMap(LocalDateTime startDate, LocalDateTime endDate) {
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

    private String encodeQrcodeToBase64(BufferedImage bufferedImage) {
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

    public void validateQrCode(String base64QrCode) {

        QrPayload qrPayload = getQrPayload(base64QrCode);
        validatePeriod(qrPayload.getStartDate(), qrPayload.getEndDate());
    }

    private QrPayload getQrPayload(String base64QrCode) {

        Map<DecodeHintType, Object> hints = getDecodeHints();
        String decodeQrContent = getDecodeQrContent(base64QrCode, hints);

        try {
            return objectMapper.readValue(decodeQrContent, QrPayload.class);
        } catch (JsonProcessingException e) {
            log.error("QrCodeManager.getQrPayload", e);
            throw new GlobalRuntimeException(VisitationErrorCode.OBJECTMAPPER);
        }
    }

    private Map<DecodeHintType, Object> getDecodeHints() {
        return new EnumMap<>(DecodeHintType.class) {{
            put(DecodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
        }};
    }

    private String getDecodeQrContent(String base64QrCode, Map<DecodeHintType, Object> hints) {

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] imageBytes = decoder.decode(base64QrCode);

        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            Result decode = new QRCodeReader().decode(binaryBitmap, hints);

            return decode.getText();
        } catch (IOException e) {
            log.error("QrCodeManager.getDecodeQrContent", e);
            throw new GlobalRuntimeException(VisitationErrorCode.IO);
        } catch (ChecksumException | NotFoundException | FormatException e) {
            log.error("QrCodeManager.getDecodeQrContent", e);
            throw new GlobalRuntimeException(VisitationErrorCode.QR_DECODE);
        }
    }
}
