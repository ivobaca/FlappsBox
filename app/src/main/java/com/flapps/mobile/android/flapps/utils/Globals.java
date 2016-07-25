package com.flapps.mobile.android.flapps.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

import com.flapps.mobile.android.flapps.ClockInOutActivity;
import com.flapps.mobile.android.flapps.MainActivity;
import com.flapps.mobile.android.flapps.data.ClockInOutBean;
import com.flapps.mobile.android.flapps.data.TimerecordBean;

public class Globals {
	
	static int month = 0;
	static int day = 0;
	static int year = 0;
	
	public static boolean firstresume = true;
	public static boolean aftersave = false;
	
	public static String datafile = "datafile.xml";
	public static String statusfile = "statusfile.xml";
	public static String timerecordfile = "timerecordfile.xml";
	public static String expensesfile = "expensesfile.xml";
	public static String absencesfile = "absencesfile.xml";
	public static String activitiesfile = "activitiesfile.json";
	public static String clientsfile = "clientsfile.json";
	public static String projectsfile = "projectsfile.json";
	public static String user = "user.json";
	public static String checkinout = "checkinout.json";
	public static String checkstate = "checkstate.json";

	public static Document data = null;

	public static Calendar date = null; 
	
	public static String domain = "";
	public static String login = "";
	public static String password = "";
	public static List<String> cookies = null;
//	public static String domain = "test23";
//	public static String login = "admin";
//	public static String password = "386ed57";
	public static String firstname = "";
	public static String phone = "";
	public static String lastname = "";
	
	public static TimerecordBean timerecord = null;
	
	public static MainActivity mainac = null;
	
	public static boolean exit = false;
    public static boolean scrollToSignIn = false;

    public static String server = "";
    
    public static String tempdata = "bbb";
    
    public static ClockInOutActivity clockactivity = null;
    
    public static HashMap<String,Long> recentnfc;
	public static HashMap<String, ClockInOutBean> statebeans;
}
