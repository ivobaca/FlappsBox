package com.flapps.mobile.android.flapps.data.attendance;

import java.io.Serializable;

/**
 * Created by ivan on 31.5.16.
 */
class Lunches extends Breaks  implements Serializable {

	public Lunches() {
		defaulttype = Break.BreakType.LUNCH;
	}
}

