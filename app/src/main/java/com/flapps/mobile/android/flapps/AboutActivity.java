package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import com.flapps.mobile.android.flapps.config.AppConfig;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		drawer_item = 6;
        setContentView(R.layout.activity_about);
		super.onCreate(savedInstanceState);
        setTitle(R.string.activity_about_title);
        
        String versionName = "undefined";
        try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        TextView tv = (TextView) findViewById(R.id.about_version);
        tv.setText(tv.getText()+" "+versionName);
        
	}
	
	public void gotoSupportPage ( View view ) {
        
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.getSupportPage()));
        startActivity(browserIntent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void gotoSupportContact ( View view ) {
        
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { AppConfig.getSupportContact() });
		intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.activity_about_email_subject) );
		intent.putExtra(Intent.EXTRA_TEXT, "\n\n\nSent from Flapps for Android");
		startActivity(Intent.createChooser(intent, ""));
		
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//        startActivity(browserIntent);
	}
}
