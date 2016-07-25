package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import com.flapps.mobile.android.flapps.data.AppStatus;
import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AccountActivity extends BasicActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getActionBar().hide();
		AppStatus as = new AppStatus(AccountActivity.this);
		as.setShowIntro("shown");

	}

	public void skip(View v) {
        Globals.scrollToSignIn = true;
        /*
		Intent intent = new Intent(getApplicationContext(),   
                LoginActivity.class);  
    	startActivity(intent);
    	 */
    	finish();
	}
	
	public void create(View v) {
        /*
		Intent intent = new Intent(getApplicationContext(),   
				LoginActivity.class);  
    	startActivity(intent);
    	 */
    	finish();
	}
}
