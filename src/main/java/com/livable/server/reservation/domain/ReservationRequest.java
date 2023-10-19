package com.livable.server.reservation.domain;

import com.livable.server.core.exception.GlobalRuntimeException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationRequest {


    @Getter
    public static class DateQuery {

        private final LocalDate startDate;
        private final LocalDate endDate;

        public DateQuery(LocalDate startDate, LocalDate endDate) {
            validateRange(startDate, endDate);
            this.startDate = startDate;
            this.endDate = endDate;
        }

        private void validateRange(LocalDate startDate, LocalDate endDate) {
            if (startDate.isAfter(endDate)) {
                throw new GlobalRuntimeException(ReservationErrorCode.INVALID_DATE_RANGE);
            }
        }
    }
}
