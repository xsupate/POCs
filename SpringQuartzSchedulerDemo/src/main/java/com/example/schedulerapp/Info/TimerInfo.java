package com.example.schedulerapp.Info;

import lombok.Data;

@Data
public class TimerInfo {
	private int totolFireCount;
	private int remainingFireCount;
	private boolean runForever;
	private long repeatIntervalMs;
	private long initialOffsetMs;
	private String callbackData;
}
