package com.livable.server.menu.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuChoiceResultDateRange {

  	private LocalDate startDate;
	private LocalDate endDate;

	public static MenuChoiceResultDateRange getDateRange(LocalDate referenceDate) {

	  	LocalDate startDate;
	  	LocalDate endDate;

		startDate = referenceDate.minusDays(
				referenceDate.getDayOfWeek().getValue() - 1L)
			.minusWeeks(1);

		endDate = startDate.plusDays(6);

		return MenuChoiceResultDateRange.builder()
			.startDate(startDate)
			.endDate(endDate)
			.build();
	}

}
