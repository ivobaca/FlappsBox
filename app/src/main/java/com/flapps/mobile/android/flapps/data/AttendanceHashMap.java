package com.flapps.mobile.android.flapps.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * Created by ivan on 27.5.16.
 */
public class AttendanceHashMap extends HashMap<Long, AttendanceBean> {

	public AttendanceHashMap( JSONArray arr ) {

		super();

		if ( arr != null ) {
			for ( int i = 0; i < arr.size(); i++ ) {
				AttendanceBean ab = null;
				ab = new AttendanceBean((JSONObject) arr.get(i));
				put(ab.getId(), ab);
			}
		}
	}
}
