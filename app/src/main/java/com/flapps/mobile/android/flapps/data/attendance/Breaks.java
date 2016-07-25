package com.flapps.mobile.android.flapps.data.attendance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ivan on 31.5.16.
 */
class Breaks extends ArrayList<Break> implements Serializable {

	protected Break.BreakType defaulttype = Break.BreakType.BREAK;
	private Break current;

	public void start() {
		current = new Break(defaulttype);
		current.start();
	}

	public void stop() {
		if ( current == null ) return;
		current.stop();
		add( current );
		current = null;
	}

	public long getLength() {

		long length = 0l;
		Iterator<Break> i = iterator();
		while ( i.hasNext() ) {
			length += i.next().getLength();
		}
		if ( current != null ) length += current.getLength();

		return length;
	}
}

