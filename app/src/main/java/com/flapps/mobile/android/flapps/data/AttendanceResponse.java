package com.flapps.mobile.android.flapps.data;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AttendanceResponse {
	
	protected ClockStateBean state;
	protected boolean partial = false;
	protected ArrayList<ClockInOutBean> updates;
	
	public AttendanceResponse( JSONObject obj ) {
		
		state = new ClockStateBean( (JSONObject)obj.get("state") );
		partial = (Boolean)obj.get("JSONObject");
		updates = new ArrayList<ClockInOutBean>();
		
		JSONArray all = (JSONArray)obj.get("updates");
		for( int i = 0; i<all.size(); i++ ) {
			updates.add( new ClockInOutBean((JSONObject)all.get(i)) );
		}
	}

	public ClockStateBean getState() {
		return state;
	}

	public void setState(ClockStateBean state) {
		this.state = state;
	}

	public boolean isPartial() {
		return partial;
	}

	public void setPartial(boolean partial) {
		this.partial = partial;
	}

	public ArrayList<ClockInOutBean> getUpdates() {
		return updates;
	}

	public void setUpdates(ArrayList<ClockInOutBean> updates) {
		this.updates = updates;
	}
}
