package com.livable.server.visitation.mock;

import com.livable.server.visitation.dto.VisitationRequest;

public class ValidateQrCodeCodeSuccessMockRequest extends VisitationRequest.ValidateQrCodeDto {
    private String qr;

    public ValidateQrCodeCodeSuccessMockRequest() {
    }

    public ValidateQrCodeCodeSuccessMockRequest(String qr) {
        this.qr = qr;
    }

    @Override
    public String getQr() {
        return qr;
    }
}
