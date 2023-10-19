package com.livable.server.core.util.scheduler.menuchoiceresult.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MenuChoiceTriggerListener implements org.quartz.TriggerListener {

  @Override
  public String getName() {
		return MenuChoiceTriggerListener.class.getName();
  }

  @Override
  public void triggerFired(Trigger trigger, JobExecutionContext context) {
	  JobKey jobKey = trigger.getJobKey();
	  log.info("Trigger Fired at {} : JobKey : {}", trigger.getStartTime(),jobKey);
  }

  @Override
  public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
	return false;
  }

  @Override
  public void triggerMisfired(Trigger trigger) {
	  JobKey jobKey = trigger.getJobKey();
	  log.info("Trigger MisFired at {} : JobKey : {}", trigger.getStartTime(),jobKey);
  }

  @Override
  public void triggerComplete(Trigger trigger, JobExecutionContext context,
	  CompletedExecutionInstruction triggerInstructionCode) {
	  JobKey jobKey = trigger.getJobKey();
	  log.info("Trigger Complete at {} : JobKey : {}", trigger.getStartTime(),jobKey);
  }
}
