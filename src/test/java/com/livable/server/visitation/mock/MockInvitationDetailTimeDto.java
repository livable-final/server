package com.livable.server.visitation.mock;

import com.livable.server.invitation.dto.InvitationDetailTimeDto;

import java.time.LocalDate;
import java.time.LocalTime;

public class MockInvitationDetailTimeDto implements InvitationDetailTimeDto {

    @Override
    public LocalDate getStartDate() {
        return LocalDate.now();
    }

    @Override
    public LocalDate getEndDate() {
        return LocalDate.now();
    }

    @Override
    public LocalTime getStartTime() {
        return LocalTime.of(1, 10);
    }

    @Override
    public LocalTime getEndTime() {
        return LocalTime.of(1, 20);
    }
}
