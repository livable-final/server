package com.livable.server.visitation.dto;

import com.beust.ah.A;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitationResponse {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class InvitationTimeDto {
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime startTime;
        private LocalTime endTime;

        public LocalDateTime getStartDateTime() {
            return LocalDateTime.of(startDate, startTime);
        }

        public LocalDateTime getEndDateTime() {
            return LocalDateTime.of(endDate, endTime);
        }
    }
}
