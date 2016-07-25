package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.flapps.mobile.android.flapps.data.AbsenceRequestBean;
import com.flapps.mobile.android.flapps.data.AbsenceTypeHashMap;
import com.flapps.mobile.android.flapps.data.DataConnector;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.ExpenseTypeHashMap;
import com.flapps.mobile.android.flapps.utils.Globals;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AbsenceNewActivity extends BackIconActivity implements OnItemSelectedListener{
	   
	private Calendar c;
	private TextView tv;
	private AbsenceRequestBean arb;
	   
	private Menu mainmenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_absencenew);
		
		arb = new AbsenceRequestBean();
		arb.setEndDate(Calendar.getInstance());
		arb.setStartDate(Calendar.getInstance());

		TextView startdate = (TextView) findViewById(R.id.absencenew_startdate);
		TextView enddate = (TextView) findViewById(R.id.absencenew_enddate);
		
		startdate.setText(arb.getStartDate().get(Calendar.YEAR)+"-"+(arb.getStartDate().get(Calendar.MONTH)+1)+"-"+arb.getStartDate().get(Calendar.DAY_OF_MONTH));
		enddate.setText(arb.getEndDate().get(Calendar.YEAR)+"-"+(arb.getEndDate().get(Calendar.MONTH)+1)+"-"+arb.getEndDate().get(Calendar.DAY_OF_MONTH));
		
		Switch switchStart = (Switch) findViewById(R.id.absencenew_startdate_on_off);
		// attach a listener to check for changes in state
		switchStart.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
			boolean isChecked) {
	
				if (isChecked)	arb.setStartLength(2);
				else arb.setStartLength(1);
			}
		});
 
		Switch switchEnd = (Switch) findViewById(R.id.absencenew_enddate_on_off);
		// attach a listener to check for changes in state
		switchEnd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
			boolean isChecked) {
	
				if (isChecked)	arb.setEndLength(2);
				else arb.setEndLength(1);
	
			}
		});
 
		AbsenceTypeHashMap athm = new DataModel(AbsenceNewActivity.this).getAbsenceTypes();
		Spinner spinner = (Spinner) findViewById(R.id.absencenew_type);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item,  athm.getStringArray());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		EditText note = (EditText) findViewById(R.id.absencenew_note);
		note.clearFocus();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.absencenew, menu);
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
	     case R.id.action_send:

	    	 EditText etn = (EditText)findViewById(R.id.absencenew_note);
	    	 arb.setNote( etn.getText().toString() );
	    	 
	    	 if ( arb.check() ) {
	    		 if ( arb.getExtId() == 0 ) arb.setExtId( Math.round ( 1000000 + ( Math.random() * 500000 ) ) ); 
				try {
					arb.save( AbsenceNewActivity.this );
					finish();
				} catch (Exception e) {
					 Toast.makeText(AbsenceNewActivity.this, getResources().getString(R.string.activity_absencenew_save_failed), Toast.LENGTH_LONG).show();
				}
	    	 }
			else Toast.makeText(AbsenceNewActivity.this, getResources().getString(R.string.activity_absencenew_empty_fields), Toast.LENGTH_LONG).show();
	     	
	         return true;
	
	
	     default:
	         return super.onOptionsItemSelected(item);
	     }
	}  


 
	public void onDateSelect(View view) {
		tv = (TextView) view;

		DatePickerDialog DPD;
		if ( tv.getId() == R.id.absencenew_startdate ) {
			DPD = new DatePickerDialog(
					AbsenceNewActivity.this, mDateSetListener, arb.getStartDate().get(Calendar.YEAR), arb.getStartDate().get(Calendar.MONTH), arb.getStartDate().get(Calendar.DAY_OF_MONTH));
			
		} else {
			DPD = new DatePickerDialog(
					AbsenceNewActivity.this, mDateSetListener, arb.getEndDate().get(Calendar.YEAR), arb.getEndDate().get(Calendar.MONTH), arb.getEndDate().get(Calendar.DAY_OF_MONTH));
		}
		DPD.show();
			
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		 
		  public void onDateSet(DatePicker view, int year, int monthOfYear,
		    int dayOfMonth) {

			  Calendar c = Calendar.getInstance();
			  c.set(year, monthOfYear, dayOfMonth);

			  if ( tv.getId() == R.id.absencenew_startdate ) { 
				  arb.setStartDate(c);
			  } else {
				  arb.setEndDate(c);
			  }
			  
			  if (arb.getStartDate().getTime().compareTo(arb.getEndDate().getTime()) > 0 ) arb.setEndDate(arb.getStartDate());
			  
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			  TextView tv1 = (TextView)findViewById(R.id.absencenew_startdate);
			  tv1.setText(sdf.format(arb.getStartDate().getTime()));
			  TextView tv2 = (TextView)findViewById(R.id.absencenew_enddate);
			  tv2.setText(sdf.format(arb.getEndDate().getTime()));
		  }
		 };

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		AbsenceTypeHashMap athm = new DataModel(AbsenceNewActivity.this).getAbsenceTypes();
		arb.setAbsenceTypeId( athm.getPosition(arg2).getId() );
		arb.setName(athm.getPosition(arg2).getName());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void afterSaveFailed( String ret ) {
		//deleteFile(Globals.absencesfile);
		Toast.makeText(AbsenceNewActivity.this, "Failed", Toast.LENGTH_LONG).show();
		
	}
	
	public void afterSave( String ret ) {
		if ( "OK".equals(ret) )
			Toast.makeText(AbsenceNewActivity.this, "Saved", Toast.LENGTH_LONG).show();
		else Toast.makeText(AbsenceNewActivity.this, "Failed: "+ret, Toast.LENGTH_LONG).show();

		deleteFile(Globals.absencesfile);
		new DataConnector(AbsenceNewActivity.this).execute("reloadAbsences", "");
		
	}
	
	public void afterReload( String result) {
		//DataModel dm = new DataModel( this );
		try {
			DataModel.saveFile( AbsenceNewActivity.this, result );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}
	

		
}
