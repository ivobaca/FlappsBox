package com.flapps.mobile.android.flapps.data;

import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * Created by ivan on 27.5.16.
 */
public class AttendanceBean implements Serializable,Comparable<AttendanceBean> {

	private Long id;
	private String title;
	private boolean active = false;

	public AttendanceBean(JSONObject att) {

		id = (Long) att.get( "activityId" );
		title = (String) att.get( "activityName" );
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int compareTo(AttendanceBean o) {

		if ( id == 0 ) return 1;
		if ( o.getId() == 0 ) return -1;
		if ( "work".equals( getTitle().toLowerCase() ) ) return -1;
		if ( "work".equals( o.getTitle().toLowerCase() ) ) return 1;
		return (int)(id - o.getId());
	}
}
