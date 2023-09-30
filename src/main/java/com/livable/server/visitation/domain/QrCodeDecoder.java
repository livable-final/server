package com.livable.server.visitation.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.livable.server.core.exception.GlobalRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class QrCodeDecoder {
    private static final String DEFAULT_CHARSET = "UTF-8";

    private final ObjectMapper objectMapper;

    public QrPayload getQrPayload(final String base64QrCode) {

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

    private String getDecodeQrContent(final String base64QrCode, final Map<DecodeHintType, Object> hints) {

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
