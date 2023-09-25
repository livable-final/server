package com.livable.server.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class AvailableReservationTimeProjection {

    private LocalDate date;
    private LocalTime time;
}
