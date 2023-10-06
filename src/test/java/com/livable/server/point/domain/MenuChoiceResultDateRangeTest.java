package com.livable.server.point.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class MenuChoiceResultDateRangeTest {

    @DisplayName("생성자 테스트 - 성공")
    @Test
    void success_Test() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 23, 4, 12);
        LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 4, 12);

        LocalDateTime expectedStartDate = LocalDateTime.of(2023, 1, 23, 0, 0);
        LocalDateTime expectedEndDate = LocalDateTime.of(2023, 2, 23, 0, 0);

        // When
        DateRange actual = new DateRange(startDate, endDate);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedStartDate, actual.getStartDate()),
                () -> Assertions.assertEquals(expectedEndDate, actual.getEndDate())
        );
    }
}