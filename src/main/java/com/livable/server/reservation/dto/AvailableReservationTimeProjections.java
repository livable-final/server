package com.livable.server.reservation.dto;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AvailableReservationTimeProjections {

    List<AvailableReservationTimeProjection> projections;

    public List<ReservationResponse.AvailableReservationTimePerDateDto> toDto() {
        return projections.stream()
                .collect(Collectors.groupingBy(
                        AvailableReservationTimeProjection::getDate,
                        Collectors.mapping(AvailableReservationTimeProjection::getTime, Collectors.toList())
                ))
                .entrySet()
                .stream()
                .map(entry -> ReservationResponse.AvailableReservationTimePerDateDto
                        .builder()
                        .date(entry.getKey())
                        .availableTimes(entry.getValue())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
