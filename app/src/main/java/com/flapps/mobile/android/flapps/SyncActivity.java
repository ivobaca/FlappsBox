package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import java.io.IOException;

import com.flapps.mobile.android.flapps.data.AppStatus;
import com.flapps.mobile.android.flapps.data.DataConnector;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.DataSync;
import com.flapps.mobile.android.flapps.utils.Beeper;
import com.flapps.mobile.android.flapps.utils.Globals;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SyncActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		drawer_item = 5;
		setContentView(R.layout.activity_sync);
		super.onCreate(savedInstanceState);
		setTitle(R.string.activity_sync_title);
		
		final AppStatus as = new AppStatus(SyncActivity.this);
		
		TextView time = (TextView) findViewById(R.id.sync_time);
		int progress = as.getSyncInterval();
		if (progress<0) progress = 0;
    	if ( progress < 59 ) {
    		time.setText( (progress + 1)+" "+getResources().getString(R.string.units_minutes) );
    	} else {
    		time.setText( (progress - 58)+" "+getResources().getString(R.string.units_hours) );
    	}
		TextView last = (TextView) findViewById(R.id.sync_last);
		last.setText( getResources().getString( R.string.activity_sync_text_lastsync ) + " " + as.getLastSync() );
		
		
		final SeekBar sb = (SeekBar) findViewById(R.id.seek_bar);
		if (as.getSyncInterval() > -1) sb.setProgress(as.getSyncInterval());
		else sb.setProgress(0);
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progressChanged = 0;
 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
            	TextView tv = (TextView) findViewById(R.id.sync_time);
            	if ( progressChanged < 59 ) {
            		tv.setText( (progressChanged + 1)+" "+getResources().getString(R.string.units_minutes) );
            	} else {
            		tv.setText( (progressChanged - 58)+" "+getResources().getString(R.string.units_hours) );
            	}
            }
 
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
 
            public void onStopTrackingTouch(SeekBar seekBar) {
            	as.setSyncInterval(progressChanged);
            }
        });
		
		Switch mySwitch = (Switch) findViewById(R.id.sync_on_off);

		// set the switch to ON
		if (as.getSyncInterval() > -1) mySwitch.setChecked(true);
		else  mySwitch.setChecked(false);
		// attach a listener to check for changes in state
		mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
			boolean isChecked) {
	
				if (isChecked) {
		
					as.setSyncInterval(sb.getProgress());
		
				} else {
		
					as.setSyncInterval(-1);
				}
	
			}
		});
 
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
//		new Beeper(this);
//		SystemClock.sleep(5000);
//		new Beeper(this, true);
	}



	public void syncNow( View view ) {
		//new DataConnector(SyncActivity.this).execute("reloadSync", "");
		new DataSync(SyncActivity.this).execute("syncnow", "");
	}
	
	public void afterReload (String result) {
		//DataModel dm = new DataModel(this);
		try {
			DataModel.saveFile( SyncActivity.this, result);
			deleteFile(Globals.absencesfile);
			deleteFile(Globals.timerecordfile);
			deleteFile(Globals.expensesfile);
			deleteFile(Globals.clientsfile);
			deleteFile(Globals.projectsfile);
			deleteFile(Globals.activitiesfile);
			deleteFile(Globals.checkinout);
			Toast.makeText(SyncActivity.this, getString(R.string.activity_main_sync_success), Toast.LENGTH_LONG).show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
