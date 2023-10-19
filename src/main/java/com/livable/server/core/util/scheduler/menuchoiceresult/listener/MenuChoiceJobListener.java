package com.livable.server.core.util.scheduler.menuchoiceresult.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MenuChoiceJobListener implements org.quartz.JobListener {

  @Override
  public String getName() {
	return MenuChoiceJobListener.class.getName();
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext context) {
	  JobKey jobKey = context.getJobDetail().getKey();
	  log.info("Job Executed : JobKey : {}",jobKey);
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext context) {
	  JobKey jobKey = context.getJobDetail().getKey();
	  log.info("Job ExecutionVetoed : JobKey : {}",jobKey);
  }

  @Override
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
	  JobKey jobKey = context.getJobDetail().getKey();
	  log.info("Job Was Executed : JobKey : {}",jobKey);
  }
}
