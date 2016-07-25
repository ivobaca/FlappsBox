package com.flapps.mobile.android.flapps.utils;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.flapps.mobile.android.flapps.data.AppStatus;

/**
 * Created by ivan on 22.6.16.
 */
public class LogUtils {

	public static void logUser( Context context ) {

		AppStatus as = new AppStatus( context );
		String[] creds = as.getCredentials();
		Crashlytics.setUserIdentifier( creds[0] + "/" + creds[1] );
	}

}
