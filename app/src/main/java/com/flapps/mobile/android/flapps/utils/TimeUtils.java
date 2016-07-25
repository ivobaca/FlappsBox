package com.flapps.mobile.android.flapps.utils;

public class TimeUtils {
    
	public static String formatTime( long time ) {
		
		time /= 1000;
		int hour = (int) Math.floor(time / 3600);
		int minute = (int) Math.floor((time - (hour * 3600)) / 60);
		int second = (int) (time - (minute * 60) - (hour * 3600));
		return String.format("%02d", hour) + ":"
				+ String.format("%02d", minute) + ":"
				+ String.format("%02d", second);
	}
}

   
    
    