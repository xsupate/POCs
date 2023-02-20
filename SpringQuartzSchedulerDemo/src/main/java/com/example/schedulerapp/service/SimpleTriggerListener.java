package com.example.schedulerapp.service;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;

import com.example.schedulerapp.Info.TimerInfo;

import org.quartz.TriggerListener;

public class SimpleTriggerListener implements TriggerListener {
	private final SchedulerService schedulerService;
	
	public SimpleTriggerListener(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}

	@Override
	public String getName() {
		return SimpleTriggerListener.class.getSimpleName();
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		final String timerId = trigger.getKey().getName();
		final JobDataMap map = context.getJobDetail().getJobDataMap();
		final TimerInfo info = (TimerInfo)map.get(timerId);
		if(!info.isRunForever() && info.getRemainingFireCount() > 0) {
			info.setRemainingFireCount(info.getRemainingFireCount() - 1);
		}
        schedulerService.updateTimer(timerId, info);
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		final String timerId = trigger.getKey().getName();
		final JobDataMap map = context.getJobDetail().getJobDataMap();
		final TimerInfo info = (TimerInfo)map.get(timerId);
		
		// skip job execution if remaining fire count is 3
		if(!info.isRunForever() && info.getRemainingFireCount() == 3) {
			return true;
		}
		
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		// TODO Auto-generated method stub

	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		// TODO Auto-generated method stub

	}

}
