package com.flapps.mobile.android.flapps;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONObject;

import com.flapps.mobile.android.flapps.adapters.CalendarListAdapter;
import com.flapps.mobile.android.flapps.adapters.ClockInOutListAdapter;
import com.flapps.mobile.android.flapps.data.AppStatus;
import com.flapps.mobile.android.flapps.data.ClockInOutBean;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.NfcSync;
import com.flapps.mobile.android.flapps.data.TimerecordBean;
import com.flapps.mobile.android.flapps.utils.Globals;
import com.flapps.mobile.android.flapps.utils.LogUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class ClockInOutActivity extends DrawerActivity {

	private TimerTask mReloadTask;
	final Handler reloadHandler = new Handler();
	Timer t2 = new Timer();
	private boolean reloadTimerRuns = false;

	@Override
    protected void onCreate(Bundle savedInstanceState) {

		//  for crashlytics
		LogUtils.logUser( ClockInOutActivity.this );

		drawer_item = 0;
		
        Log.v("ClockInOutActivity", "create");
        setContentView(R.layout.activity_clockinout);
        super.onCreate(savedInstanceState);
        
        Globals.clockactivity = this;
        
        /*
        TextView temp = (TextView) findViewById(R.id.tempdata);
        temp.setText(Globals.tempdata);
        Log.v("CIO", "TEmpdata:" + Globals.tempdata);
        */
        
		try {
			DataModel.saveFile( ClockInOutActivity.this, "<?xml version=\"1.0\" encoding=\"utf-8\"><flapps></flapps>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		refreshList();

		AppStatus as = new AppStatus(ClockInOutActivity.this);
		as.setShowIntro("shown");
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (reloadTimerRuns)
			mReloadTask.cancel();
		AppStatus as = new AppStatus(ClockInOutActivity.this);
		int interval = as.getSyncInterval();
//		interval = 1;
		if (interval >= 0) {
			if (interval < 60)
				interval = ((interval + 1) * 60) * 1000;
			else
				interval = (((interval - 59) + 1) * 60 * 60) * 1000;
			mReloadTask = new TimerTask() {
				public void run() {
					reloadHandler.post(new Runnable() { 
						public void run() {

//							new DataConnector(MainActivity.this).execute(
//									"reload", "");
							NfcSync nfcs = new NfcSync(ClockInOutActivity.this);
							nfcs.activity = ClockInOutActivity.this;
							nfcs.execute("","");
						}
					});
				}
			};
			t2.schedule(mReloadTask, reloadTimerRuns?interval:0, interval); //
			reloadTimerRuns = true;
		}
	}



	public void refreshList() {
		Log.v( "CIOU AUCTIVITY", "IN 2" );
		
        DataModel dm = new DataModel(ClockInOutActivity.this);
        ArrayList<ClockInOutBean> data = new ArrayList<ClockInOutBean>();
        Collections.sort(data);
		try {
			//data = dm.readAttendanceHistory();
			data = dm.readAttendance();
	        Collections.sort(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ListView list = (ListView)findViewById(R.id.attendance_list);
        list.setAdapter(new ClockInOutListAdapter(ClockInOutActivity.this,
				R.layout.clockinout_list_item, data));

        /*
         * a.clear();
         
        a.addAll(data);
        a.notifyDataSetChanged();
        */
		
	}
	
	@Override
	public void onBackPressed() {
		// mTimerTask.cancel();
		// mReloadTask.cancel();
		super.onBackPressed();
		this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	private void storeAction() {
		
        JSONObject obj = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        obj.put("created", sdf.format(new Date()));
        obj.put("cardId", id);
        obj.put("activityId", activity );

        dm.storeCheckinout(obj);

	}*/

}
