package com.flapps.mobile.android.flapps.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.content.Context;

import com.flapps.mobile.android.flapps.data.ApiConfigBean;
import com.flapps.mobile.android.flapps.data.AttendanceHashMap;
import com.flapps.mobile.android.flapps.data.FileManager;
import com.flapps.mobile.android.flapps.data.MenuConfigBean;

public class ApiConfig extends FileManager {
	
	public final static String FILENAME_MENU = "config_menu.dat";
	public final static String FILENAME_CONFIG = "config_CONFIG.dat";
	public final static String FILENAME_ATT_OPS = "config_att_ops.dat";

	public ApiConfig(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void save( JSONObject obj ) throws IOException {
		
		JSONArray menu = (JSONArray) obj.get("menu");
		if ( menu != null ) {
			saveObject( new MenuConfigBean( menu ), FILENAME_MENU);
		}
		
		JSONObject config = (JSONObject) obj.get("config");
		if ( config != null ) {
			saveObject( new ApiConfigBean( config ), FILENAME_CONFIG);
		}
		
		JSONArray attendenceOperations = (JSONArray) obj.get("attendenceOperations");
		AttendanceHashMap ahm = new AttendanceHashMap( attendenceOperations );
		saveObject( ahm, FILENAME_ATT_OPS);
	}
	
	public MenuConfigBean getMenuConfig() throws IOException, ClassNotFoundException {
		return (MenuConfigBean)readObject(FILENAME_MENU);
	}
	
	public ApiConfigBean getConfig() throws IOException, ClassNotFoundException {
		return (ApiConfigBean)readObject(FILENAME_CONFIG);
	}

	public AttendanceHashMap getAttendanceOps() throws IOException, ClassNotFoundException {
		return (AttendanceHashMap) readObject(FILENAME_ATT_OPS);
	}

}
