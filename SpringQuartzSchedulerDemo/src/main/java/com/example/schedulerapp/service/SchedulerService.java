package com.example.schedulerapp.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.schedulerapp.Info.TimerInfo;
import com.example.schedulerapp.utils.TimerUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class SchedulerService {
	private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

	private final Scheduler scheduler;

	@Autowired
	public SchedulerService(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void schedule(final Class jobClass, final TimerInfo info) {
		final JobDetail jobDetail = TimerUtils.buildJobDetail(jobClass, info);
		final Trigger trigger = TimerUtils.buildTrigger(jobClass, info);
		
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		}catch(SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public List<TimerInfo> getAllRunningTimers(){
		try {
			return scheduler.getJobKeys(GroupMatcher.anyGroup())
					.stream()
					.map(jobkey -> {
						try {
							final JobDetail jobDetail = scheduler.getJobDetail(jobkey);
							return (TimerInfo) jobDetail.getJobDataMap().get(jobkey.getName());
						} catch (SchedulerException e) {
							logger.error(e.getMessage(), e);
							return null;
						}
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		}catch(SchedulerException e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}
	
	public TimerInfo getRunningTimer(final String timerId) {
		try {
			final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
			if(jobDetail == null) {
				logger.error("Failed to find timer with ID " + timerId);
				return null;	
			}
			return (TimerInfo) jobDetail.getJobDataMap().get(timerId);
		}catch(SchedulerException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public void updateTimer(final String timerId, final TimerInfo info) {
		try {
			final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
			if(jobDetail == null) {
				logger.error("Failed to find timer with ID " + timerId);
				return ;	
			}
			jobDetail.getJobDataMap().put(timerId, info);
		}catch(SchedulerException e) {
			logger.error(e.getMessage(), e);
			return;
		}
	}
	
	public Boolean deleteTimer(final String timerId) {
		try {
			return scheduler.deleteJob(new JobKey(timerId));
		}catch(SchedulerException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	@PostConstruct
	public void init() {
		try {
			scheduler.start();
			scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener(this));
		}catch(SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@PreDestroy
	public void preDestroy() {
		try {
			scheduler.shutdown();
		}catch(SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
