package com.flapps.mobile.android.flapps.data.attendance;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ivan on 31.5.16.
 */
class Break implements Serializable {

	public BreakType type = BreakType.BREAK;
	private Date start;
	private Date end;

	public Break() {

	}

	public Break( BreakType type ) {
		this.type = type;
	}

	public void start() {
		start = new Date();
	}

	public void stop () {
		end = new Date();
	}

	long getLength() {

		if ( start == null ) return 0;
		if ( end == null ) return new Date().getTime() - start.getTime();
		return end.getTime() - start.getTime();
	}

	public enum BreakType  implements Serializable {
		BREAK, LUNCH, WORK
	}
}

