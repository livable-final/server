package com.livable.server.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PresentDateValidator implements ConstraintValidator<PresentDate, LocalDate> {
  @Override
  public boolean isValid(LocalDate date, ConstraintValidatorContext context) {

	if (date == null) {
	  return false;
	}

	LocalDate currentDate = LocalDateTime.now().toLocalDate();

	return date.isEqual(currentDate);

  }
}
