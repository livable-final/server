package com.livable.server.reservation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationResponse {

    @Getter
    @Builder
    public static class AvailableReservationTimePerDateDto {
        private LocalDate date;
        private List<LocalTime> availableTimes;
    }
}
