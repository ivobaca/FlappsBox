package com.flapps.mobile.android.flapps.data;

import java.io.Serializable;

import org.apache.http.conn.scheme.SocketFactory;
import org.json.simple.JSONObject;

public class ClockInOutBean implements Serializable, Comparable<ClockInOutBean>{

	protected long time;
	protected Long activityId;
	protected String cardId;
	protected String name;
	
	public ClockInOutBean( JSONObject obj ) {
		
		name = (String) obj.get("cardDisplay");
		if ( name == null ) name = "";
		time = (Long) obj.get("time");
		//if ( json_time != null ) time = Long.parseLong(json_time); 
		
		activityId = (Long) obj.get("activityId");
		//if ( json_activity != null ) activityId = Integer.parseInt(json_activity); 
		
		cardId = (String) obj.get("cardId");
	}
	
	public ClockInOutBean() {
		name = "";
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("time", ""+time);
		if ( cardId != null ) obj.put("cardId", ""+cardId);
		if ( name != null ) obj.put("cardDisplay", ""+name);
		if ( activityId != null ) obj.put("activityId", ""+activityId );
		
		return obj;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(ClockInOutBean another) {

		return (int) ( another.getTime() - getTime() );
	}
	
	
}
