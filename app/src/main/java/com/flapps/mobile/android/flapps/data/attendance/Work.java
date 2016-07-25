package com.flapps.mobile.android.flapps.data.attendance;

import com.flapps.mobile.android.flapps.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ivan on 31.5.16.
 */
public class Work implements Serializable {

	public Works works = new Works();
	public Breaks breaks = new Breaks();
	public Lunches lunches = new Lunches();

	public Date start;
	private Date end;
	private State state = State.OUT;

	public void start() {

		if ( start != null ) {
			if (!DateUtils.isToday(start)) {
				start = new Date();
				works = new Works();
			}
		} else {
			start = new Date();
			works = new Works();
		}
		state = State.WORK;
		works.start();
	}

	public void stop() {
		end = new Date();
		state = State.OUT;
		works.stop();
	}

	public long getLength() {

		long length = 0;
		if ( start == null ) return 0;
		if ( !DateUtils.isToday(start) ) return 0;

		length = works.getLength();

		return length;
	}

	public void goForLunch() {
		lunches.start();
		state = State.LUNCH;
	}

	public void returnFromLunch() {
		lunches.stop();
		state = State.WORK;
	}

	public void goForBreak() {
		breaks.start();
		state = State.BREAK;
	}

	public void returnFromBreak() {
		breaks.stop();
		state = State.WORK;
	}

	public State getState() {
		return state;
	}

	private enum State  implements Serializable {
		WORK, BREAK, LUNCH, OUT
	}


}

