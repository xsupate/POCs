package com.example.schedulerapp.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.schedulerapp.Info.TimerInfo;

public class HelloWorldJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(HelloWorldJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		TimerInfo info = (TimerInfo)map.get(context.getJobDetail().getJobClass().getSimpleName());
		logger.info("Execution HelloWorldJob and data " + info.getCallbackData());
		logger.info("Remaining fire count '{}'", info.getRemainingFireCount());
	}

}
