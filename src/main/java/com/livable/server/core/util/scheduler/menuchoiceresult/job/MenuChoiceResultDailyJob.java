package com.livable.server.core.util.scheduler.menuchoiceresult.job;

import com.livable.server.menu.service.MenuChoiceResultService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@RequiredArgsConstructor
@Slf4j
public class MenuChoiceResultDailyJob implements Job {

  	private final MenuChoiceResultService menuChoiceResultService;

	@Override
	public void execute(JobExecutionContext context) {

	  //Date of Job executed
	  Long milliseconds = context.getFireTime().getTime();

	  LocalDate referenceDate = millisecondsToDate(milliseconds);

	  menuChoiceResultService.createDailyMenuChoiceResult(referenceDate);

  	}

	private LocalDate millisecondsToDate(Long milliseconds) {
	  Instant instant = Instant.ofEpochMilli(milliseconds);

	  ZoneId zoneId = ZoneId.systemDefault();
	  return instant.atZone(zoneId).toLocalDate();
	}


}
