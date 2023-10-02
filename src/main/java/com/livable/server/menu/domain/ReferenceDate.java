package com.livable.server.menu.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReferenceDate {

	START_WITH_MONDAY(1);

	private final Integer dayOfWeek;

	public static LocalDate getReferenceDate(LocalDate localDate, ReferenceDate referenceDate) {
		return localDate.minusDays(
				localDate.getDayOfWeek().getValue() - referenceDate.getDayOfWeek())
			.minusWeeks(1);
	}
}
