package com.livable.server.visitation.mock;

import com.livable.server.visitation.dto.VisitationRequest;

public class ValidateQrCodeSuccessMockRequest extends VisitationRequest.ValidateQrDto {
    private String qr;

    public ValidateQrCodeSuccessMockRequest() {
    }

    public ValidateQrCodeSuccessMockRequest(String qr) {
        this.qr = qr;
    }

    @Override
    public String getQr() {
        return qr;
    }
}
