package com.example.schedulerapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.schedulerapp.Info.TimerInfo;
import com.example.schedulerapp.jobs.HelloWorldJob;

@Service
public class PlaygroundService {
     private final SchedulerService schedulerService;
     
     @Autowired
     public PlaygroundService(final SchedulerService schedulerService) {
    	 this.schedulerService = schedulerService;
     }
     
     
     public void runHelloWorldJob() {
    	 final TimerInfo info = new TimerInfo();
    	 info.setTotolFireCount(5);
    	 info.setRemainingFireCount(info.getTotolFireCount());
    	 info.setRepeatIntervalMs(2000);
    	 info.setInitialOffsetMs(1000);
    	 info.setCallbackData("Its callback data");
    	 
    	 schedulerService.schedule(HelloWorldJob.class, info);
     }
     
     
     public Boolean deleteTimer(String timerId) {
    	 return schedulerService.deleteTimer(timerId);
     }
     
     public List<TimerInfo> getAllRunningTimers(){
    	 return schedulerService.getAllRunningTimers();
     }


	public TimerInfo getRunningTimer(String timerId) {
		return schedulerService.getRunningTimer(timerId);
	}
}
