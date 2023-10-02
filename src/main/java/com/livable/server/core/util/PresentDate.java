package com.livable.server.core.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = PresentDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PresentDate {
  String message() default "Date는 오늘 날짜와 같아야 합니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}