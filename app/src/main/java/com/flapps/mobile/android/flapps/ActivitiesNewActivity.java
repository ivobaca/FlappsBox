package com.flapps.mobile.android.flapps;

import java.io.IOException;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.flapps.mobile.android.flapps.R;
import com.flapps.mobile.android.flapps.data.ActivityBean;
import com.flapps.mobile.android.flapps.data.ClientBean;
import com.flapps.mobile.android.flapps.data.DataModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class ActivitiesNewActivity extends BackIconActivity  {

	   private Menu mainmenu;
	   private ActivityBean ab;

	   @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activitiesnew);

		/*
		Spinner spinner = (Spinner) findViewById(R.id.activity_project);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.activities_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		*/
		
		Bundle b = getIntent().getExtras();
		final int value = b.getInt("id");
		if ( value != 0) {
			ab = new DataModel(ActivitiesNewActivity.this).getActivities().get(value);
			
			EditText et1 = (EditText) findViewById(R.id.activity_name);
			et1.setText(ab.getName());
			//EditText et2 = (EditText) findViewById(R.id.activity_name);
			//et2.setText(ab.getRate());
			/*
			if ( ab.isWorkedTime() ) {
				Switch s = (Switch) findViewById(R.id.activity_time_worked);
				s.setChecked(true);
			}
			if ( ab.isActive() ) {
				Switch s = (Switch) findViewById(R.id.activity_active);
				s.setChecked(true);
			}
			EditText et3 = (EditText) findViewById(R.id.activity_note);
			et3.setText(ab.getNote());
			
			if ( "forbidden".equals( ab.getProject() ) )
				spinner.setSelection( 0 );
			else if ( "allowed".equals( ab.getProject() ) )
				spinner.setSelection( 1 );
			else spinner.setSelection( 2 );
			*/
		} else {
			Random rand = new Random();

		    // nextInt is normally exclusive of the top value,
		    // so add 1 to make it inclusive
		    int randomNum = (rand.nextInt((10000 - 1) + 1) + 1) * -1;
			ab = new ActivityBean();
			ab.setId( randomNum );
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activitiesnew, menu);
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
	     	
	     	EditText et1 = (EditText) findViewById(R.id.activity_name);
	     	ab.setName(et1.getText().toString());
	     	
			try {
				ab.save(ActivitiesNewActivity.this);
				finish();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(ActivitiesNewActivity.this, "TransformerConfigurationException", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(ActivitiesNewActivity.this, "IOException", Toast.LENGTH_LONG).show();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(ActivitiesNewActivity.this, "ParserConfigurationException", Toast.LENGTH_LONG).show();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(ActivitiesNewActivity.this, "SAXException", Toast.LENGTH_LONG).show();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(ActivitiesNewActivity.this, "TransformerException", Toast.LENGTH_LONG).show();
			}
	         return true;
	
	
	     default:
	         return super.onOptionsItemSelected(item);
	     }
	}


}
