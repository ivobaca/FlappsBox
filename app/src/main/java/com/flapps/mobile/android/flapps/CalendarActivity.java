package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.exina.android.calendar.CalendarView;
import com.exina.android.calendar.Cell;
import com.flapps.mobile.android.flapps.adapters.CalendarListAdapter;
import com.flapps.mobile.android.flapps.data.DataConnector;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.TimerecordBean;
import com.flapps.mobile.android.flapps.utils.Globals;
import com.google.android.apps.dashclock.ui.SwipeDismissListViewTouchListener;

public class CalendarActivity extends BackIconActivity implements
		CalendarView.OnCellTouchListener {

	CalendarView mView = null;
	Handler mHandler = new Handler();
	
	   private Menu mainmenu;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_right_out);
		setContentView(R.layout.activity_calendar);
		setTitle(R.string.activity_calendar_titel);

		TextView tv = (TextView) findViewById(R.id.calendar_month);
		DateFormat dateFormat = new SimpleDateFormat("MMMM yyyy",
				Locale.ENGLISH);
		;
		tv.setText(dateFormat.format(Globals.date.getTime()));

		mView = (CalendarView) findViewById(R.id.calendar);
		mView.setOnCellTouchListener(this);

		ListView lv = (ListView) findViewById(R.id.calendar_list);
		// TimerecordBean[] data = new DataModel( CalendarActivity.this
		// ).getTimerecords( Globals.date ).getTimerecordBeanArray();
		ArrayList data = new DataModel(CalendarActivity.this).getTimerecords(
				Globals.date).getTimerecordBeanList();
		// Log.v("calendarview", ""+data.length);
		lv.setAdapter(new CalendarListAdapter(CalendarActivity.this,
				R.layout.calendar_list_item, data));

		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				lv, new SwipeDismissListViewTouchListener.OnDismissCallback() {
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						final ArrayAdapter<TimerecordBean> adapter = (ArrayAdapter<TimerecordBean>) listView
								.getAdapter();
						for (final int position : reverseSortedPositions) {
							if (adapter.getItem(position) != null) {

								final TimerecordBean trb = adapter
										.getItem(position);
								adapter.remove(trb);
								new AlertDialog.Builder(CalendarActivity.this)
										.setTitle(
												getResources()
														.getString(
																R.string.activity_calendar_remove_titel))
										.setMessage(
												getResources()
														.getString(
																R.string.activity_calendar_remove_hint))
										.setIcon(
												android.R.drawable.ic_dialog_alert)
										.setPositiveButton(
												android.R.string.yes,
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														// Toast.makeText(CalendarActivity.this,
														// "Yaay",
														// Toast.LENGTH_SHORT).show();
														
														try {
															trb.remove(
																	CalendarActivity.this,
																	Globals.date);
														} catch (Exception e) {
															Toast.makeText(CalendarActivity.this, "Failed remove. ",
																	Toast.LENGTH_LONG).show();
															Log.v("REMOVE", "Message: "+e.getMessage());
															e.printStackTrace();
														}
													}
												})
										.setNegativeButton(
												android.R.string.no,
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														// Toast.makeText(CalendarActivity.this,
														// "No",
														// Toast.LENGTH_SHORT).show();
														adapter.insert(trb,
																position);
													}
												}).show();

							}
						}
						adapter.notifyDataSetChanged();
						listView.invalidate();
					}

					@Override
					public boolean canDismiss(int position) {
						// TODO Auto-generated method stub
						// Toast.makeText(CalendarActivity.this, "can",
						// Toast.LENGTH_LONG).show();
						return true;
					}
				});
		lv.setOnTouchListener(touchListener);
		lv.setOnScrollListener(touchListener.makeScrollListener());
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// data.get(position);
				Globals.timerecord = (TimerecordBean) parent
						.getItemAtPosition(position);
				 Globals.mainac.nCounter = Globals.timerecord.getDuration() * 60;
		    	 if ( DateUtils.isToday(Globals.date.getTimeInMillis()) ) Globals.mainac.buttonState = "start";
		    	 else Globals.mainac.buttonState = "save";
				CalendarActivity.this.finish();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.costs, menu);
		mainmenu = menu;
		
		return true;
	}


	   /**
	  * Event Handling for Individual menu item selected
	  * Identify single menu item by it's id
	  * */
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item)
	 {
	     switch (item.getItemId())
	     {
	     case R.id.action_new:
	    	 
	    	 Globals.timerecord = new TimerecordBean();
	    	 Globals.mainac.nCounter = 0;
	    	 Globals.mainac.nStartCounter = new Date().getTime();
	    	 if (Globals.mainac.timerRuns) Globals.mainac.mTimerTask.cancel();
	    	 Globals.mainac.timerRuns = false;
	    	 if ( DateUtils.isToday(Globals.date.getTimeInMillis()) ) Globals.mainac.buttonState = "start";
	    	 else Globals.mainac.buttonState = "save";
	    	 finish();
	     	
	         return true;
	
	
	     default:
	         return super.onOptionsItemSelected(item);
	     }
	}  


	public void onTouch(Cell cell) {
		int year = mView.getYear();
		int month = mView.getMonth();
		int day = cell.getDayOfMonth();

		Globals.date.set(year, month, day);
		ListView lv = (ListView) findViewById(R.id.calendar_list);
		ArrayList<TimerecordBean> data = new DataModel(CalendarActivity.this)
				.getTimerecords(Globals.date).getTimerecordBeanList();
		lv.setAdapter(new CalendarListAdapter(CalendarActivity.this,
				R.layout.calendar_list_item, data));
		lv.invalidate();

		mView.setDay(cell);
		mView.invalidate();

		mHandler.post(new Runnable() {
			public void run() {
				// Toast.makeText(CalendarActivity.this,
				// DateUtils.getMonthString(mView.getMonth(),
				// DateUtils.LENGTH_LONG) + " "+mView.getYear(),
				// Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void CalendarUp(View v) {

		mView.nextMonth();
		mView.invalidate();

		setCalendarMonthView();
	}

	public void CalendarDown(View v) {

		mView.previousMonth();
		mView.invalidate();

		setCalendarMonthView();
	}

	protected void setCalendarMonthView() {
		TextView tv = (TextView) findViewById(R.id.calendar_month);
		DateFormat dateFormat = new SimpleDateFormat("MMMM yyyy",
				Locale.ENGLISH);
		Calendar c = Calendar.getInstance();
		c.set(mView.getYear(), mView.getMonth(), 1);

		tv.setText(dateFormat.format(c.getTime()));
	}

	public void afterRemove(String result) {
		if ("OK".equals(result))
			Toast.makeText(CalendarActivity.this, "Removed", Toast.LENGTH_LONG)
					.show();
		else
			Toast.makeText(CalendarActivity.this, "Failed: " + result,
					Toast.LENGTH_LONG).show();

		deleteFile(Globals.timerecordfile);
		new DataConnector(CalendarActivity.this).execute("reloadRemoved", "");
	}

	public void afterReload(String result) {
		//DataModel dm = new DataModel(this);
		try {
			DataModel.saveFile( CalendarActivity.this, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.swipe_left_in, R.anim.swipe_left_out);

	}

}
