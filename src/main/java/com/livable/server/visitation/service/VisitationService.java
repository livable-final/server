package com.livable.server.visitation.service;

import com.livable.server.visitation.domain.QrCodeManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class VisitationService {

    private final QrCodeManager qrCodeManager;

    public String createQrCode(final LocalDateTime startDate, final LocalDateTime endDate) {
        return qrCodeManager.createQrCode(startDate, endDate);
    }

    public void validateQrCode(final String qr) {
        qrCodeManager.validateQrCode(qr);
    }
}
