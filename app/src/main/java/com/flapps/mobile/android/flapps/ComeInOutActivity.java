package com.flapps.mobile.android.flapps;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.flapps.mobile.android.flapps.adapters.AttendanceListAdapter;
import com.flapps.mobile.android.flapps.config.ApiConfig;
import com.flapps.mobile.android.flapps.data.AttendanceHashMap;
import com.flapps.mobile.android.flapps.data.ClockInOutBean;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.attendance.Work;
import com.flapps.mobile.android.flapps.utils.DateUtils;
import com.flapps.mobile.android.flapps.utils.LogUtils;
import com.flapps.mobile.android.flapps.utils.TimeUtils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.json.simple.JSONArray;

public class ComeInOutActivity extends DrawerActivity implements OnClickListener {
	
	private Timer mTimer = new Timer();
	private boolean mTimerruns = false;
	private TimerTask mTimerTask;
	private Handler mTimerHandler = new Handler();
	private GridLayoutManager lLayout;
	private RecyclerView rv;
	
	private Long current_running;
	private Date current_time;
	private Date current_work_time;
	private boolean current_work_running = false;

	AttendanceHashMap operations;
	
	private Work mWork = new Work();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//  for crashlytics
		LogUtils.logUser( ComeInOutActivity.this );

		drawer_item = 1;
		setContentView(R.layout.activity_comeinout);
		super.onCreate(savedInstanceState);
        setTitle(R.string.activity_comeinout_title);
        
        TextView textdate = (TextView)findViewById(R.id.textview_date);
        SimpleDateFormat dt = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        textdate.setText(dt.format(new Date()));
        
        TextView texttime = (TextView)findViewById(R.id.textview_time);
        SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm");
        texttime.setText(dt2.format(new Date()));

        TextView textphase = (TextView)findViewById(R.id.phase_label);
        textphase.setText("Work");

        TextView textphasetime = (TextView)findViewById(R.id.phase_time);
        textphasetime.setText("00:00:00");

        mTimerTask = new TimerTask() {
			public void run() {
				mTimerHandler.post(new Runnable() {
					public void run() {

						setTimeFields();
					}
				});
			}
		};
		mTimer.schedule( mTimerTask, 0, 1000);
		mTimerruns = true;
		
		try {
			operations = new ApiConfig( this ).getAttendanceOps();
		} catch (Exception e) {
			operations = new AttendanceHashMap( new JSONArray() );
		}

		setButtons();
	}

	@Override
	protected void onPause() {
		super.onPause();

		HashMap<Long, Long> store = new HashMap<Long, Long>();
		store.put( 0l, current_running);
		if ( current_time != null ) store.put( 1l, current_time.getTime() );
		if ( current_work_time != null ) store.put( 2l, current_work_time.getTime() );
		if ( current_work_running ) store.put( 3l, new Long( 1l ) );

		try {
			new DataModel( ComeInOutActivity.this ).storeComeInOutState( store );
			new DataModel( ComeInOutActivity.this ).storeComeInOutWork( mWork );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		HashMap<Long, Long> store = null;
		try {
			store = new DataModel( ComeInOutActivity.this ).readComeInOutState();

			if ( store != null ) {
				current_running = store.get(new Long(0l));
				Long l1 = store.get(new Long(1l));
				if ( l1 != null ) current_time = new Date( l1 ); else current_time = null;
				Long l2 = store.get(new Long(2l));
				if ( l2 != null ) current_work_time = new Date( l2 ); else current_work_time = null;
				Long l3 = store.get( new Long(3l) );
				if ( l3 != null ) current_work_running = true; else current_work_running = false;

				if ( current_running != null) {
					if ( current_running > 1l ) {
						operations.get( current_running ).setActive( true );
						TextView textphase = (TextView)findViewById(R.id.phase_label);
						textphase.setText(operations.get( current_running ).getTitle());

					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			mWork = new DataModel( ComeInOutActivity.this ).readComeInOutWork();
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

	private void setButtons() {
		
		lLayout = new GridLayoutManager( ComeInOutActivity.this, 2);
		rv = (RecyclerView)findViewById(R.id.recycler_view);
		rv.setLayoutManager(lLayout);
		rv.setAdapter( new AttendanceListAdapter( ComeInOutActivity.this , operations, this) );

		/*
		LinearLayout ll = (LinearLayout)findViewById(R.id.button_wrapper);
		Iterator<Long> i = operations.keySet().iterator();
		while( i.hasNext() ) {
			
			Long key = (Long)i.next();
			
			if ( key == null ) continue;
			if ( (key == -1) || (key == 0) || (key == 1) ) continue;
			
			String value = operations.get(key);
			
			Button b = new Button( this );
			b.setText(value);
			b.setTag(key);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		            LayoutParams.WRAP_CONTENT);
			b.setLayoutParams(params);
			b.setGravity(Gravity.CENTER_HORIZONTAL);
		
			b.setOnClickListener( this );
			ll.addView(b, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, 
                    LayoutParams.WRAP_CONTENT));
			
		}
			*/
	}
	
	public void setTimeFields() {
		
		TextView texttime = (TextView)findViewById(R.id.textview_time);
        SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm");
        texttime.setText(dt2.format(new Date()));

    	TextView textphasetime = (TextView)findViewById(R.id.phase_time);
        if ( current_running != null && current_time != null ) {
        	long timervalue = new Date().getTime() - current_time.getTime();
        	textphasetime.setText( TimeUtils.formatTime(timervalue) );
        } else {
        	if ( current_work_running && current_work_time != null ) {
            	//long timervalue = new Date().getTime() - current_work_time.getTime();
		        long timervalue = mWork.getLength();
            	textphasetime.setText( TimeUtils.formatTime(timervalue) );
        	}
        }
	}
	
	/*
	public void setTimeFields() {
		
		TextView texttime = (TextView)findViewById(R.id.textview_time);
        SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm");
        texttime.setText(dt2.format(new Date()));
        
        long timervalue = 0;
        if ( mWork.getState() == State.OUT ) {
        	timervalue = 0;
        } else if ( mWork.getState() == State.WORK ) {
        	timervalue = mWork.getLength();
        } else if ( mWork.getState() == State.BREAK ) {
        	timervalue = mWork.breaks.getLength();
        } else if ( mWork.getState() == State.LUNCH ) {
        	timervalue = mWork.lunches.getLength();
        }
        TextView textphasetime = (TextView)findViewById(R.id.phase_time);
        textphasetime.setText( TimeUtils.formatTime(timervalue) );
	}
	
	
	public void clockInOutButtonAction( View v ) {
		
        TextView textphase = (TextView)findViewById(R.id.phase_label);
        textphase.setText("Work");
		Button button = (Button)findViewById(R.id.clock_button);
		if ( mWork.getState() == State.OUT ) {
			mWork.start();
			button.setText("Clock Out");
		} else {
			mWork.stop();
			button.setText("Clock In");
			mWork = new Work();
		}
	}
	
	public void breakButtonAction( View v ) {
		
		Button button = (Button)findViewById(R.id.break_button);
        TextView textphase = (TextView)findViewById(R.id.phase_label);
		if ( mWork.getState() == State.BREAK ) {
			mWork.returnFromBreak();
			button.setText("Take the break ");
	        textphase.setText("Work");
		} else {
			mWork.goForBreak();
			button.setText("Stop the break");
	        textphase.setText("Break");
		}
	}
	
	public void lunchButtonAction( View v ) {
		
		Button button = (Button)findViewById(R.id.lunch_button);
        TextView textphase = (TextView)findViewById(R.id.phase_label);
		if ( mWork.getState() == State.LUNCH ) {
			mWork.returnFromLunch();
			button.setText("Out for Lunch");
	        textphase.setText("Work");
		} else {
			mWork.goForLunch();
			button.setText("Back from Lunch");
	        textphase.setText("Lunch");
		}
	}
	*/



	@Override
	public void onClick(View v) {
		
		Long tag = (Long)v.getTag();

		onClick(tag);
		
	}


	public void onClick( Long tag ) {

		TextView textphase = (TextView)findViewById(R.id.phase_label);
		if ( tag == current_running ) {
			return;
		}
		operations.get( tag ).setActive( true );
		if ( tag != 0 ) textphase.setText( operations.get(tag).getTitle() );
		sendAttendanceActivity( tag, 0l );
		if ( "work".equals( operations.get( tag ).getTitle().toLowerCase() ) ) {
			current_work_running = true;
			current_work_time = new Date();
			current_time = null;
			current_running = null;
			mWork.start();
		} else {
			mWork.stop();
			if ( tag == 0 ) {
				current_work_running = false;
				current_running = null;
			} else {
				current_work_running = false;
				current_running = tag;
				current_time = new Date();

			}
		}

	}
	/*
	public void onClick( Long tag ) {
		TextView textphase = (TextView)findViewById(R.id.phase_label);
		if ( tag == current_running ) {
			textphase.setText("Time Worked");
			if (current_work_running) {
				mWork.start();
			}
			current_running = null;
			current_time = null;
			sendAttendanceActivity( tag, 0l );
			operations.get( tag ).setActive( false );
		} else {

			if ( current_running != null && current_time != null) {
				sendAttendanceActivity( current_running, 0l );
				operations.get( current_running ).setActive( false );
			}
			textphase.setText( operations.get(tag).getTitle() );

			if (current_work_running) {
				mWork.stop();
			}

			current_running = tag;
			current_time = new Date();
			sendAttendanceActivity( tag, 0l );
			operations.get( tag ).setActive( true );
		}

		rv.getAdapter().notifyDataSetChanged();
	}*/
	
	private void sendAttendanceActivity( Long id, Long card  ) {
		
		
        DataModel dm = new DataModel(this);

        ClockInOutBean ciob = new ClockInOutBean();
        ciob.setTime(new Date().getTime());
        ciob.setActivityId(id);
        try {
			dm.addUpdateAttendance(ciob);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        /*
         
        JSONObject obj = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        obj.put("created", sdf.format(new Date()));
        obj.put("activityId", id);
        //obj.put("cardId", id);
        
        DataModel dm = new DataModel(this);
        try {
			dm.storeCheckinout(obj);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*/
	}
	
	public void clockInButtonAction( View v ) {

		if ( current_running != null && current_time != null) {
			sendAttendanceActivity( current_running, 0l );
			operations.get( current_running ).setActive( false );
		}
		sendAttendanceActivity( -1l, 0l );

		if ( current_work_running != true  || mWork.start == null ) {
			if (current_work_time == null) current_work_time = new Date();
			current_work_running = true;
			current_running = null;
			mWork.start();


			rv.getAdapter().notifyDataSetChanged();
		}
		//Button b1 = (Button)findViewById(R.id.clock_out_button);
		//b1.setVisibility(View.VISIBLE);
		//Button b2 = (Button)findViewById(R.id.clock_in_button);
		//b2.setVisibility(View.GONE);
	}
	
	public void clockOutButtonAction( View v ) {
		if ( current_running != null && current_time != null) {
			sendAttendanceActivity( current_running, 0l );
			operations.get( current_running ).setActive( false );
		}
		sendAttendanceActivity( 0l, 0l );

		if ( current_work_running != false ) {
			//current_work_time = null;
			current_running = null;
			current_work_running = false;
			current_time = null;
			mWork.stop();

			TextView textphase = (TextView) findViewById(R.id.phase_label);
			textphase.setText("Time Worked");

			rv.getAdapter().notifyDataSetChanged();
		}
		//Button b1 = (Button)findViewById(R.id.clock_out_button);
		//b1.setVisibility(View.GONE);
		//Button b2 = (Button)findViewById(R.id.clock_in_button);
		//b2.setVisibility(View.VISIBLE);
	}
	
	   /**
	  * Event Handling for Individual menu item selected
	  * Identify single menu item by it's id
	  * */
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item)
	 {
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}

			switch (item.getItemId())
	     {
	     case R.id.action_new:
	     	
	         return true;
	
	
	     default:
	         return super.onOptionsItemSelected(item);
	     }
	}  

}
