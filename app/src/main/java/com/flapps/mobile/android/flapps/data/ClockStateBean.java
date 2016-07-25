package com.flapps.mobile.android.flapps.data;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class ClockStateBean implements Serializable {
	
	protected long time;
	protected String place;
	
	public ClockStateBean( JSONObject obj ) {

		String json_time = (String) obj.get("time");
		if ( json_time != null ) time = Long.parseLong(json_time); 
		
		place = (String) obj.get("place");
	}
	
	public JSONObject toJSON() {
		
		JSONObject obj = new JSONObject();
		obj.put("time", ""+time);
		obj.put("place", place);
		
		return obj;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	
	
}
