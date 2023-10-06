package com.livable.server.core.util.scheduler.menuchoiceresult.service;

import static org.quartz.CronScheduleBuilder.cronSchedule;

import com.livable.server.core.util.scheduler.menuchoiceresult.domain.CronSchedule;
import com.livable.server.core.util.scheduler.menuchoiceresult.job.MenuChoiceResultWeeklyJob;
import com.livable.server.core.util.scheduler.menuchoiceresult.listener.MenuChoiceJobListener;
import com.livable.server.core.util.scheduler.menuchoiceresult.listener.MenuChoiceTriggerListener;
import com.livable.server.core.util.scheduler.menuchoiceresult.job.MenuChoiceResultDailyJob;
import java.util.TimeZone;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuChoiceQuartzService {

  private final SchedulerFactoryBean schedulerFactory;

  private static final String GROUP_NAME = "Menu Choice Result";

  @PostConstruct
  public void scheduled() {

	Scheduler scheduler = schedulerFactory.getScheduler();

	JobDetail menuChoiceResultDailyJob = menuChoiceResultJobDetail(MenuChoiceResultDailyJob.class);
	CronTrigger menuChoiceResultDailyCronTrigger = menuChoiceResultCronTrigger(menuChoiceResultDailyJob, CronSchedule.EVERY_DAY_OF_ZERO.getCroneSchedule(), "가장 많이 선택된 메뉴 집계 Daily Trigger");

	JobDetail menuChoiceResultWeeklyJob = menuChoiceResultJobDetail(MenuChoiceResultWeeklyJob.class);
	CronTrigger menuChoiceResultWeeklyCronTrigger = menuChoiceResultCronTrigger(menuChoiceResultWeeklyJob, CronSchedule.EVERY_SUNDAY_OF_END.getCroneSchedule(), "가장 많이 선택된 메뉴 집계 WeeklyTrigger");

	try {
		//Listener Setting
		ListenerManager listenerManager = scheduler.getListenerManager();
		listenerManager.addJobListener(new MenuChoiceJobListener());
		listenerManager.addTriggerListener(new MenuChoiceTriggerListener());

		//Add Job & Trigger to schedule
		scheduler.scheduleJob(menuChoiceResultDailyJob, menuChoiceResultDailyCronTrigger);
		scheduler.scheduleJob(menuChoiceResultWeeklyJob, menuChoiceResultWeeklyCronTrigger);

	} catch (SchedulerException e) {
		log.error("Menu Choice Result Scheduler Exception Occurred : {}", menuChoiceResultDailyJob.getKey());
	}
  }

  public JobDetail menuChoiceResultJobDetail(Class <? extends Job> clazz) {

	  return JobBuilder.newJob(clazz)
		  .withIdentity(UUID.randomUUID().toString(), GROUP_NAME)
		  .withDescription(clazz.getSimpleName())
		  .build();
  }

  public CronTrigger menuChoiceResultCronTrigger(JobDetail jobDetail, String schedule, String description) {
	return TriggerBuilder.newTrigger()
		.forJob(jobDetail)
		.withIdentity(jobDetail.getKey().getName(), GROUP_NAME)
		.withDescription(description)
		.withSchedule(cronSchedule(schedule)
			.inTimeZone(TimeZone.getDefault())
			.withMisfireHandlingInstructionFireAndProceed()
		)
		.build();
  }

}
