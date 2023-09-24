package com.livable.server.point.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Component
public class DateFactory {

    /**
     * 기준이 되는 날짜 정보를 받아 해당 month의 시작과 끝 범위의 데이터를 반환한다.
     * @param localDateTime
     * @return 한달 범위의 시작과 끝 날짜 데이터
     */
    public DateRange getMonthRangeOf(LocalDateTime localDateTime) {

        LocalDateTime startDate = localDateTime.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime endDate = localDateTime.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());

        return new DateRange(startDate, endDate);
    }
}
