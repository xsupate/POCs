package com.example.schedulerapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.schedulerapp.Info.TimerInfo;
import com.example.schedulerapp.service.PlaygroundService;

@RestController
@RequestMapping("/api/timer")
public class PlaygroundController {
	@Autowired
	private PlaygroundService  playgroundService;
	
	@PostMapping("/runhelloworldjob")
	public void runHelloWorldJob() {
		playgroundService.runHelloWorldJob();
	}
	
	@GetMapping("/getTimers")
	public List<TimerInfo> getAllRunningTimmers(){
		return playgroundService.getAllRunningTimers();
	}
	
	@GetMapping("/{timerId}")
	public TimerInfo getRunningTimer(@PathVariable String timerId) {
		return playgroundService.getRunningTimer(timerId);
	}
	
	@DeleteMapping("/{timerId}")
	public Boolean deleteTimer(@PathVariable String timerId) {
		return playgroundService.deleteTimer(timerId);
	}
}
