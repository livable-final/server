package com.livable.server.core.util.scheduler.menuchoiceresult.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CronSchedule {

  	EVERY_DAY_OF_ZERO("0 0 0 1/1 * ? *"),
  	EVERY_SUNDAY_OF_END("0 45 23 ? * SUN *");

	private final String croneSchedule;
}
