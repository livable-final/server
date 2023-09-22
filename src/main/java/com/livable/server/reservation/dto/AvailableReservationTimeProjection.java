package com.livable.server.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@ToString
public class AvailableReservationTimeProjection {

    LocalDate date;
    LocalTime time;
}
