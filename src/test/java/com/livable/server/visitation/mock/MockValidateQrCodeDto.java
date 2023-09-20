package com.livable.server.visitation.mock;

import com.livable.server.visitation.dto.VisitationRequest;

public class MockValidateQrCodeDto extends VisitationRequest.ValidateQrCodeDto {
    private String qr;

    public MockValidateQrCodeDto() {
    }

    public MockValidateQrCodeDto(String qr) {
        this.qr = qr;
    }

    @Override
    public String getQr() {
        return qr;
    }
}
