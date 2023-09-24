package com.livable.server.point.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class DateFactoryTest {

    private DateFactory dateFactory;

    @BeforeEach
    void setUp() {
        dateFactory = new DateFactory();
    }

    @DisplayName("한달의 시작과 끝을 반환하는 메서드 테스트 - 성공")
    @Test
    void getMonthRange_Success_Test() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 2, 1, 0, 0);
        LocalDateTime localDateTime = LocalDateTime.of(2023, 1, 1, 1, 1);

        // When
        DateRange actual = dateFactory.getMonthRangeOf(localDateTime);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(startDate, actual.getStartDate()),
                () -> Assertions.assertEquals(endDate, actual.getEndDate())
        );
    }
}