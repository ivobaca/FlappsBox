package com.flapps.mobile.android.flapps.config;

import com.flapps.mobile.android.flapps.utils.Globals;

public class AppConfig {
	
	static int FLAPPS_LOCATION_WORLD = 0;
	static int FLAPPS_LOCATION_SLOVAKIA = 1;
	static int FLAPPS_LOCATION_FRANCE = 2;
	static int FLAPPS_LOCATION_ITALY = 3;
	static int FLAPPS_LOCATION_GERMANY = 4;
	
	//	change to get different location version of the app
	static int FLAPPS_LOCATION = FLAPPS_LOCATION_WORLD;
	
	public static String getApiUrl () {
		
		return Globals.server;
		/*
		switch ( FLAPPS_LOCATION ) {
			case 1: return "https://my.flapps.sk";
			case 2: return "https://my.flapps.fr";
			case 3: return "https://my.flapps.it";
			case 4: return "https://de.test4.aston.sk";
			default: return "https://my.flapps.com";
		}
		*/
	}
	
	public static String getApiUrlFallback () {
		
		return Globals.server;
		/*
		switch ( FLAPPS_LOCATION ) {
			case 1: return "https://my.flapps.eu";
			case 2: return "https://my.flapps.eu";
			case 3: return "https://my.flapps.eu";
			case 4: return "https://my.flapps.eu";
			default: return "https://my.flapps.com";
		}
		*/
	}
	
	public static boolean isApiProtocolHttps () {
		//if ( FLAPPS_LOCATION == FLAPPS_LOCATION_ITALY ) return false;
		return true;
	}
	
	public static String getSupportPage() {
		switch ( FLAPPS_LOCATION ) {
		//		case 1: return "https://my.flapps.sk";
		//		case 2: return "https://my.flapps.fr";
		case 3: return "https://www.flapps.it";
		case 4: return "https://www.flapps.eu";
			default: return "http://www.flapps.com";
		}
	}
	
	public static String getSupportContact() {
		switch ( FLAPPS_LOCATION ) {
		//		case 1: return "https://my.flapps.sk";
		//		case 2: return "https://my.flapps.fr";
		case 3: return "support@flapps.it";
		case 4: return "support@flapps.eu";
			default: return "support@flapps.com";
		}
	}
}
