package com.livable.server.visitation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface InvitationTimeDto {

    LocalTime getStartTime();

    LocalTime getEndTime();
    LocalDate getStartDate();

    LocalDate getEndDate();

}
