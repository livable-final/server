package com.livable.server.visitation.mock;

import com.livable.server.visitation.domain.QrPayload;

import java.time.LocalDateTime;

public class MockQrPayload extends QrPayload {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public MockQrPayload(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public LocalDateTime getStartDate() {
        return startDate;
    }

    @Override
    public LocalDateTime getEndDate() {
        return endDate;
    }
}
