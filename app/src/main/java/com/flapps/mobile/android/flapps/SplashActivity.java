package com.flapps.mobile.android.flapps;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import java.io.IOException;
import java.util.ArrayList;

import com.flapps.mobile.android.flapps.R;
import com.flapps.mobile.android.flapps.config.ApiConfig;
import com.flapps.mobile.android.flapps.data.AppStatus;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.MenuConfigBean;
import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class SplashActivity extends BasicActivity {

	// stopping splash screen starting home activity.
	private static final int STOPSPLASH = 0;
	// time duration in millisecond for which your splash screen should visible
	// to
	// user. here i have taken half second
	private static final long SPLASHTIME = 0;

	// handler for splash screen
	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				// Generating and Starting new intent on splash time out
				AppStatus as = new AppStatus(SplashActivity.this);
				
				Intent intent = null;
				if ( as.showIntro() ) {
					intent = new Intent(getApplicationContext(),
							IntroActivity.class);
				} else if( DataModel.exists(SplashActivity.this) ) {
					String[] crs = as.getCredentials();
					Globals.domain = crs[0];
					Globals.login = crs[1];
					Globals.password = crs[2];
					MenuConfigBean mcb = new MenuConfigBean();
					try {
						mcb = new ApiConfig(SplashActivity.this).getMenuConfig();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ArrayList<String> menuitems = mcb.getItems();
					if ( menuitems.contains("attendanceChecker") ) {
						
						intent = new Intent(getApplicationContext(), ClockInOutActivity.class);
					} else {
						intent = new Intent(getApplicationContext(), MainActivity.class);
					}
				}
				else {
                    Globals.scrollToSignIn = true;
                    intent = new Intent(getApplicationContext(),
                            IntroActivity.class);
					//intent = new Intent(getApplicationContext(), LoginActivity.class);
				}
				startActivity(intent);
				SplashActivity.this.finish();
				overridePendingTransition(R.anim.swipe_left_in, R.anim.swipe_left_out);
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
		setContentView(R.layout.activity_splash);
		getActionBar().hide();

		// Generating message and sending it to splash handle
		Message msg = new Message();
		msg.what = STOPSPLASH;
		splashHandler.sendMessageDelayed(msg, SPLASHTIME);
	}
}
