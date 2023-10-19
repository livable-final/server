package com.livable.server.point.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DateRange {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = getStartTimeOfMonth(startDate);
        this.endDate = getStartTimeOfMonth(endDate);
    }

    private LocalDateTime getStartTimeOfMonth(LocalDateTime localDateTime) {
        return localDateTime.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
