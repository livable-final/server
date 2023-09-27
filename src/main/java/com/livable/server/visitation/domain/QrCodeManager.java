package com.livable.server.visitation.domain;

import com.livable.server.core.exception.GlobalRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class QrCodeManager {

    private static final int DEFAULT_QR_CODE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_BACKGROUND_COLOR = 0xFF2563EA;

    private final QrCodeEncoder qrCodeEncoder;
    private final QrCodeDecoder qrCodeDecoder;

    public String createQrCode(final LocalDateTime startDate, final LocalDateTime endDate) {

        validatePeriod(startDate, endDate);

        BufferedImage qrCodeBufferdImage = qrCodeEncoder.createQrCodeBufferdImage(startDate, endDate);

        return qrCodeEncoder.encodeQrcodeToBase64(qrCodeBufferdImage);
    }

    private void validatePeriod(final LocalDateTime startDate, final LocalDateTime endDate) {
        if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
            throw new GlobalRuntimeException(VisitationErrorCode.INVALID_PERIOD);
        }

        if (!(startDate.isBefore(LocalDateTime.now()) && endDate.isAfter(LocalDateTime.now()))) {
            throw new GlobalRuntimeException(VisitationErrorCode.INVALID_QR_PERIOD);
        }
    }

    public void validateQrCode(final String base64QrCode) {

        QrPayload qrPayload = qrCodeDecoder.getQrPayload(base64QrCode);
        validatePeriod(qrPayload.getStartDate(), qrPayload.getEndDate());
    }
}
