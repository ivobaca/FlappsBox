package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.flapps.mobile.android.flapps.adapters.TimerecordListAdapter;
import com.flapps.mobile.android.flapps.data.ActivityHashMap;
import com.flapps.mobile.android.flapps.data.AppStatus;
import com.flapps.mobile.android.flapps.data.ClientBean;
import com.flapps.mobile.android.flapps.data.DataConnector;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.DataSync;
import com.flapps.mobile.android.flapps.data.ProjectBean;
import com.flapps.mobile.android.flapps.data.TimerecordBean;
import com.flapps.mobile.android.flapps.data.TimerecordHashMap;
import com.flapps.mobile.android.flapps.utils.DateUtils;
import com.flapps.mobile.android.flapps.utils.Globals;
import com.flapps.mobile.android.flapps.utils.LogUtils;

public class MainActivity extends DrawerActivity {

	public final static int RETURN_CODE_ACTIVITY = 0;
	public final static int RETURN_CODE_PROJECT = 1;
	public final static int RETURN_CODE_DESCRIPTION = 2;

	private Menu mainmenu;

	public TimerTask mTimerTask;
	private TimerTask mReloadTask;
	final Handler handler = new Handler();
	final Handler reloadHandler = new Handler();
	Timer t = new Timer();
	Timer t2 = new Timer();
	TextView tvCounter = null;
	private boolean reloadTimerRuns = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//  for crashlytics
		LogUtils.logUser( MainActivity.this );

		if (reloadTimerRuns)
			mReloadTask.cancel();
		AppStatus as = new AppStatus(MainActivity.this);
		int interval = as.getSyncInterval();
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
							new DataSync(MainActivity.this).execute("","");
						}
					});
				}
			};
			t2.schedule(mReloadTask, 0, interval); //
			reloadTimerRuns = true;
		}


		drawer_item = 0;
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.swipe_left_in, R.anim.swipe_left_out);
		setTitle(R.string.activity_main_title);

		Globals.mainac = this;

		//AppStatus as = new AppStatus(MainActivity.this);
		as.setShowIntro("shown");

		String[] mMainFieldTitles = getResources().getStringArray(
				R.array.main_fields_array);

		ListView mlv = (ListView) findViewById(R.id.main_list);
		mlv.setAdapter(new TimerecordListAdapter(this, R.layout.main_list_item,
				mMainFieldTitles));
		mlv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Intent i0 = new Intent(MainActivity.this,
							ActivitiesActivity.class);
					startActivityForResult(i0, RETURN_CODE_ACTIVITY);
					overridePendingTransition(R.anim.swipe_right_in,
							R.anim.swipe_left_out);
					break;
				case 1:
					Intent i1 = new Intent(MainActivity.this,
							ClientsActivity.class);
					startActivityForResult(i1, RETURN_CODE_PROJECT);
					overridePendingTransition(R.anim.swipe_right_in,
							R.anim.swipe_left_out);
					break;
				}
			}
		});
		tvCounter = (TextView) findViewById(R.id.main_timer);

		String[] mMainFieldTitlesD = getResources().getStringArray(
				R.array.main_fields_array_description);

		ListView mlvd = (ListView) findViewById(R.id.main_list_description);
		mlvd.setAdapter(new TimerecordListAdapter(this,
				R.layout.main_list_item_desc, mMainFieldTitlesD));
		mlvd.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i0 = new Intent(MainActivity.this,
						DescriptionActivity.class);
				Bundle b = new Bundle();
				b.putString("description", Globals.timerecord.getNote()); // Your
																			// id
				i0.putExtras(b);
				startActivityForResult(i0, RETURN_CODE_DESCRIPTION);
				overridePendingTransition(R.anim.swipe_right_in,
						R.anim.swipe_left_out);

			}
		});

		// setNewTimerecord();

	}

	@Override
	protected void onDestroy() {
		if (timerRuns)
			mTimerTask.cancel();
		mReloadTask.cancel();

		Globals.firstresume = true;
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// mTimerTask.cancel();
		// mReloadTask.cancel();
		super.onBackPressed();
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		mainmenu = menu;

		if (Globals.date == null)
			Globals.date = Calendar.getInstance();
		DateFormat dateFormat = DateFormat.getDateInstance();
		menu.findItem(R.id.action_date).setTitle(
				dateFormat.format(Globals.date.getTime()));
		return true;
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		DateFormat dateFormat = DateFormat.getDateInstance();
		menu.findItem(R.id.action_date).setTitle(
				dateFormat.format(Globals.date.getTime()));
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {

		case R.id.action_date:

			Intent intent2 = new Intent(getApplicationContext(),
					CalendarActivity.class);
			startActivity(intent2);
			overridePendingTransition(R.anim.swipe_right_in,
					R.anim.swipe_right_out);

			return true;

		case R.id.action_new:

			Globals.timerecord = new TimerecordBean();
			if (timerRuns)
				mTimerTask.cancel();
			nCounter = 0;
			nStartCounter = new Date().getTime();
			timerRuns = false;
			if ( android.text.format.DateUtils.isToday(Globals.date.getTimeInMillis()) ) Globals.mainac.buttonState = "start";
	    	else Globals.mainac.buttonState = "save";
			setNewTimerecord();
			return true;
			/*
			 * case R.id.action_about:
			 * 
			 * Intent intent = new Intent(getApplicationContext(),
			 * AboutActivity.class); startActivity(intent); return true;
			 * 
			 * case R.id.action_reload:
			 * 
			 * new DataConnector(MainActivity.this).execute("reload", "");
			 * return true;
			 * 
			 * case R.id.action_logout: AppStatus as = new
			 * AppStatus(MainActivity.this); as.setShowIntro("none");
			 * deleteFile(Globals.datafile); finish(); return true;
			 * 
			 * case R.id.action_datatest:
			 * 
			 * Intent intent3 = new Intent(getApplicationContext(),
			 * DataTestActivity.class); startActivity(intent3); return true;
			 */
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {

		if (Globals.timerecord == null)
			Globals.timerecord = new TimerecordBean();

		// if ( DateUtils.isToday(Globals.date) ) buttonState = "start";

		if (!Globals.firstresume) {
			if (timerRuns) {
				nCounter = (int) ((new Date().getTime() - nStartCounter) / 1000);
			}
			setNewTimerecord();
		}
		Globals.firstresume = false;

		invalidateOptionsMenu();

		setTimerButton();
		
		// DateFormat dateFormat = DateFormat.getDateInstance();
		// mainmenu.findItem(R.id.action_date).setTitle(dateFormat.format(Globals.date));
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_left_out);

	}

	protected void setNewTimerecord() {

		ListView lv = (ListView) findViewById(R.id.main_list);
		View item = lv.getChildAt(0);
		if (item == null)
			return;
		TextView tv = (TextView) item.findViewById(R.id.main_list_item_text);
		TextView tv6 = (TextView) item.findViewById(R.id.main_list_item_label);
		if (Globals.timerecord.activityId != 0) {
			tv.setText(new DataModel(this).getActivities()
					.get(Globals.timerecord.activityId).getName());
			tv6.setTextColor(getResources().getColor(R.color.main_list_full));
		} else {
			tv.setText("");
			tv6.setTextColor(getResources().getColor(R.color.main_list_empty));
		}
		View item2 = lv.getChildAt(1);
		TextView tv2 = (TextView) item2.findViewById(R.id.main_list_item_text);
		TextView tv4 = (TextView) item2
				.findViewById(R.id.main_list_item_description);
		TextView tv5 = (TextView) item2.findViewById(R.id.main_list_item_label);
		if (Globals.timerecord.projectId != 0 ) {
			Log.v( "setNewTimerecord", Globals.timerecord.projectId+"" );
			ProjectBean pb = new DataModel(this).getProjects().get(
					Globals.timerecord.projectId);
			if ( pb != null ) {
				ClientBean client = new DataModel(MainActivity.this).getClients()
						.get(pb.getClientId());
				tv2.setText(pb.getName());
				tv5.setTextColor(getResources().getColor(R.color.main_list_full));
				tv4.setText(client.getName());
				tv4.setVisibility(View.VISIBLE);
			}
		} else {
			tv2.setText("");
			tv5.setTextColor(getResources().getColor(R.color.main_list_empty));
			tv4.setVisibility(View.GONE);
		}
		lv.invalidate();
		ListView lv3 = (ListView) findViewById(R.id.main_list_description);
		View item3 = lv3.getChildAt(0);
		if (item3 != null) {
			TextView tv3 = (TextView) item3
					.findViewById(R.id.main_list_item_text);
			TextView tv7 = (TextView) item3
					.findViewById(R.id.main_list_item_label);
			if ("".equals(Globals.timerecord.getNote())) {
				tv3.setText("");
				tv7.setTextColor(getResources().getColor(
						R.color.main_list_empty));
			} else {
				tv3.setText(Globals.timerecord.getNote());
				tv7.setTextColor(getResources()
						.getColor(R.color.main_list_full));
			}
		}

		TextView timerView = (TextView) findViewById(R.id.main_timer);
//		Button b = (Button) findViewById(R.id.main_button);
		if (Globals.timerecord.duration > 0 || !DateUtils.isToday(Globals.date)) {
			double hours = Math.floor((double) Globals.timerecord.duration
					/ (double) 60);
			double minutes = (double) Globals.timerecord.duration
					- (hours * 60);

			timerView.setText(String.format("%02d", (int) hours) + ":"
					+ String.format("%02d", (int) minutes) + ":"
					+ String.format("%02d", 0));
			nCounter = Globals.timerecord.duration * 60;
			//b.setText(R.string.main_button_save);
		} else {
			timerView
					.setText(String.format("%02d", 0) + ":"
							+ String.format("%02d", 0) + ":"
							+ String.format("%02d", 0));
			//b.setText(R.string.main_button_start);
		}
		/*
		if (timerRuns) {
			b.setText(R.string.main_button_stop);
		}
		if (timerPaused) {
			b.setText(R.string.main_button_start);
		}
		*/
		setTimerButton();
	}

	public boolean timerRuns = false;
	public boolean timerPaused = false;
	public int nCounter = 0;
	public long nStartCounter = 0;
	public String buttonState = "start";

	public void controlTimer(View v) {

		/*
		Toast.makeText(MainActivity.this, "State:"+buttonState,
				Toast.LENGTH_LONG).show();
		*/
		if ("stop".equals(buttonState)) {
			if (mTimerTask != null) {
				int hour = (int) Math.floor(nCounter / 3600);
				int minute = (int) Math.floor((nCounter - (hour * 3600)) / 60);
				int second = (int) (nCounter - (minute * 60) - (hour * 3600));
				tvCounter.setText(String.format("%02d", hour) + ":"
						+ String.format("%02d", minute) + ":"
						+ String.format("%02d", second));

				mTimerTask.cancel();
				timerRuns = false;
				double minutes = (double) nCounter / (double) 60;
				double time = Math.ceil(minutes);
				Globals.timerecord.setDuration((int) time);
				//b.setText(R.string.main_button_save);
			}
			buttonState = "save";
		} else if ("start".equals(buttonState)) {
			mTimerTask = new TimerTask() {
				public void run() {
					handler.post(new Runnable() {
						public void run() {
							nCounter++;
							// update TextView
							int hour = (int) Math.floor(nCounter / 3600);
							int minute = (int) Math
									.floor((nCounter - (hour * 3600)) / 60);
							int second = (int) (nCounter - (minute * 60) - (hour * 3600));
							tvCounter.setText(String.format("%02d", hour) + ":"
									+ String.format("%02d", minute) + ":"
									+ String.format("%02d", second));

							// Log.d("TIMER", "TimerTask run");
						}
					});
					timerRuns = true;
				}
			};
			nStartCounter = new Date().getTime();
			// public void schedule (TimerTask task, long delay, long
			// period)
			t.schedule(mTimerTask, 0, 1000); //
			buttonState = "stop";
		} else 	if ("save".equals(buttonState)) {
			ActivityHashMap ash = new DataModel(MainActivity.this)
					.getActivities();
			if (Globals.timerecord.duration == 0) {
				Toast.makeText(MainActivity.this, getResources().getString(R.string.activity_main_enter_time),
						Toast.LENGTH_LONG).show();
				return;
			}
			if (Globals.timerecord.activityId == 0) {
				Toast.makeText(MainActivity.this,
						"Please, enter the activity.", Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (Globals.timerecord.projectId == 0
					&& "mandatory".equals(ash
							.get(Globals.timerecord.activityId).getProject())) {
				Toast.makeText(MainActivity.this, getResources().getString(R.string.activity_main_enter_project),
						Toast.LENGTH_LONG).show();
				return;
			}
			if ("forbidden".equals(ash.get(Globals.timerecord.activityId)
					.getProject()))
				Globals.timerecord.projectId = 0;

			Globals.timerecord.setCal(Globals.date);
			try {
				Globals.timerecord.save( MainActivity.this );
				Toast.makeText(MainActivity.this, getResources().getString(R.string.activity_main_saved), Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(MainActivity.this, getResources().getString(R.string.activity_main_save_failed) + " " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
			timerPaused = true;
			if ( DateUtils.isToday(Globals.date) ) buttonState = "start";
	    	else buttonState = "save";
		}
		
		setTimerButton();
		
		/*
		 * if ( (Globals.timerecord.getDuration() == 0 &&
		 * DateUtils.isToday(Globals.date) ) || timerRuns || timerPaused) {
		 * timerPaused = false; if (timerRuns) { if (mTimerTask != null) { int
		 * hour = (int) Math.floor(nCounter / 3600); int minute = (int) Math
		 * .floor((nCounter - (hour * 3600)) / 60); int second = (int) (nCounter
		 * - (minute * 60) - (hour * 3600));
		 * tvCounter.setText(String.format("%02d", hour) + ":" +
		 * String.format("%02d", minute) + ":" + String.format("%02d", second));
		 * 
		 * mTimerTask.cancel(); timerRuns = false;
		 * b.setText(R.string.main_button_start); double minutes = (double)
		 * nCounter / (double) 60; double time = Math.ceil(minutes);
		 * Globals.timerecord.setDuration((int) time);
		 * b.setText(R.string.main_button_save); }
		 * 
		 * } else { mTimerTask = new TimerTask() { public void run() {
		 * handler.post(new Runnable() { public void run() { nCounter++; //
		 * update TextView int hour = (int) Math.floor(nCounter / 3600); int
		 * minute = (int) Math .floor((nCounter - (hour * 3600)) / 60); int
		 * second = (int) (nCounter - (minute * 60) - (hour * 3600));
		 * tvCounter.setText(String.format("%02d", hour) + ":" +
		 * String.format("%02d", minute) + ":" + String.format("%02d", second));
		 * 
		 * // Log.d("TIMER", "TimerTask run"); } }); timerRuns = true; } };
		 * b.setText(R.string.main_button_stop); nStartCounter = new
		 * Date().getTime(); // public void schedule (TimerTask task, long
		 * delay, long // period) t.schedule(mTimerTask, 0, 1000); //
		 * 
		 * } } else {
		 * 
		 * ActivityHashMap ash = new DataModel(MainActivity.this)
		 * .getActivities(); if (Globals.timerecord.duration == 0) {
		 * Toast.makeText(MainActivity.this, "Please, enter the time.",
		 * Toast.LENGTH_LONG).show(); return; } if
		 * (Globals.timerecord.activityId == 0) {
		 * Toast.makeText(MainActivity.this, "Please, enter the activity.",
		 * Toast.LENGTH_LONG) .show(); return; } if
		 * (Globals.timerecord.projectId == 0 && "mandatory".equals(ash
		 * .get(Globals.timerecord.activityId).getProject())) {
		 * Toast.makeText(MainActivity.this, "Please, enter the project.",
		 * Toast.LENGTH_LONG).show(); return; } if
		 * ("forbidden".equals(ash.get(Globals.timerecord.activityId)
		 * .getProject())) Globals.timerecord.projectId = 0;
		 * 
		 * Globals.timerecord.save(MainActivity.this, Globals.date);
		 * b.setText(R.string.main_button_start); timerPaused = true; }
		 */
	}
	
	private void setTimerButton() {
		Button b = (Button) findViewById(R.id.main_button);
		if ( "save".equals(buttonState) ) {
			b.setText(R.string.main_button_save);
		}
		if ( "start".equals(buttonState) ) {
			b.setText(R.string.main_button_start);
		}
		if ( "stop".equals(buttonState) ) {
			b.setText(R.string.main_button_stop);
		}
	}

	public void onTimerClick(View v) {
		TimePickerFragment newFragment = new TimePickerFragment();
		newFragment.activity2 = MainActivity.this;
		newFragment.show(getFragmentManager(), "timePicker");

	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		public Activity activity2 = null;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			double hour = Math.floor((double) Globals.timerecord.duration
					/ (double) 60);
			double minute = (double) Globals.timerecord.duration
					- (double) (hour * 60);

			// Create a new instance of TimePickerDialog and return it
			TimePickerDialog tpd = new TimePickerDialog(getActivity(), this,
					(int) hour, (int) minute, true);
			tpd.setTitle(getResources().getString(
					R.string.main_timepicker_title));
			return tpd;
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (Globals.mainac.timerRuns)
				Globals.mainac.mTimerTask.cancel();
			Globals.timerecord.setDuration(minute + (hourOfDay * 60));
			//Button b = (Button) activity2.findViewById(R.id.main_button);
			TextView ttv = (TextView) activity2.findViewById(R.id.main_timer);
			((MainActivity) activity2).buttonState = "save";
			//b.setText(R.string.main_button_save);
			ttv.setText(String.format("%02d", hourOfDay) + ":"
					+ String.format("%02d", minute) + ":"
					+ String.format("%02d", 0));
			((MainActivity)getActivity()).setTimerButton();
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		ListView lv = (ListView) findViewById(R.id.main_list);
		switch (requestCode) {
		case RETURN_CODE_ACTIVITY:
			Globals.timerecord.activityId = data.getIntExtra("id", 0);
			Globals.timerecord.setBillable(data.getBooleanExtra("billable",
					false));
			setNewTimerecord();
			/*
			 * if (Globals.timerecord.activityId != 0) { View item =
			 * lv.getChildAt(0); TextView tv = (TextView) item
			 * .findViewById(R.id.main_list_item_text); tv.setText(new
			 * DataModel(this).getActivities()
			 * .get(Globals.timerecord.activityId).getName());
			 * tv.setTextColor(getResources().getColor(R.color.main_list_full));
			 * }
			 */
			break;
		case RETURN_CODE_PROJECT:
			Globals.timerecord.projectId = data.getIntExtra("id", 0);
			Log.v( "setNewTimerecord", Globals.timerecord.projectId+"" );
			setNewTimerecord();
			/*
			 * if (Globals.timerecord.projectId != 0) { View item2 =
			 * lv.getChildAt(1); TextView tv2 = (TextView) item2
			 * .findViewById(R.id.main_list_item_text); tv2.setText(new
			 * DataModel(this).getProjects()
			 * .get(Globals.timerecord.projectId).getName());
			 * tv2.setTextColor(getResources()
			 * .getColor(R.color.main_list_full)); }
			 */
			break;
		case RETURN_CODE_DESCRIPTION:
			Globals.timerecord.setNote(data.getStringExtra("description"));
			setNewTimerecord();
			/*
			 * ListView lv3 = (ListView)
			 * findViewById(R.id.main_list_description); View item3 =
			 * lv3.getChildAt(0); TextView tv3 = (TextView) item3
			 * .findViewById(R.id.main_list_item_text);
			 * tv3.setText(Globals.timerecord.getNote());
			 * tv3.setTextColor(getResources
			 * ().getColor(R.color.main_list_full));
			 */
			break;
		}
	}

	public void afterReload(String result) {
		//DataModel dm = new DataModel(this);
		if ( "".equals(result) ) {
			//Toast.makeText(MainActivity.this, getString(R.string.activity_main_sync_error), Toast.LENGTH_LONG).show();
			return;
		}
		try {
			DataModel.saveFile( MainActivity.this, result);
			deleteFile(Globals.absencesfile);
			deleteFile(Globals.timerecordfile);
			deleteFile(Globals.expensesfile);
			deleteFile(Globals.clientsfile);
			deleteFile(Globals.projectsfile);
			deleteFile(Globals.activitiesfile);
			deleteFile(Globals.checkinout);
			Toast.makeText(MainActivity.this, getString(R.string.activity_main_sync_success), Toast.LENGTH_LONG).show();
		} catch (IOException e) {}
	}
/*
	public void afterReload(String result) {
				DataModel dm = new DataModel(this);
				try {
					dm.saveFile(result);
			if (Globals.aftersave) {
				TimerecordHashMap thm = dm.getTimerecords();
				TimerecordBean tb = thm
						.getTimerecordFromExtId(Globals.timerecord.extId);
				Log.d("afterreload", "in " + Globals.timerecord.extId);
				if (tb != null)
					Globals.timerecord = tb;
				if (tb != null)
					Log.d("afterreload", "" + tb.getId());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Globals.aftersave = false;
	}
*/
			
	public void afterSave(String ret) {
		if ("OK".equals(ret))
			Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG)
					.show();
		else
			Toast.makeText(MainActivity.this, "Failed: " + ret,
					Toast.LENGTH_LONG).show();

		deleteFile(Globals.timerecordfile);
		Globals.aftersave = true;
		new DataConnector(MainActivity.this).execute("reload", "");
	}

	public void afterSaveFailed(String ret) {
		// deleteFile(Globals.timerecordfile);
		Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
	}

}
