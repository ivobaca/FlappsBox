package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;
import com.flapps.mobile.android.flapps.utils.Globals;
import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;

public class BasicActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        overridePendingTransition(R.anim.swipe_left_in, R.anim.swipe_left_out);
        getActionBar().setBackgroundDrawable(new ColorDrawable (0xff173569));
		getActionBar().setIcon(R.drawable.ic_action_bar);

		//	always on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_right_out);
	}

	@Override
	protected void onPause() {

		//overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_left_out);
		super.onPause();
	}

	@Override
	protected void onResume() {
		
		if ( Globals.exit ) {
			Globals.exit = false;
			finish();
		}
		super.onResume();
	}

	@Override
	protected void onStart() {

		super.onStart();
		FlurryAgent.onStartSession(this, "BK6VXKZ28M373QPPFXWV");
	}

	@Override
	protected void onStop() {

		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	
	
}
