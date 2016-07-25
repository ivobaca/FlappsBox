package com.flapps.mobile.android.flapps.test;

import com.flapps.mobile.android.flapps.R;
import com.flapps.mobile.android.flapps.config.ApiConfig;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import com.flapps.mobile.android.flapps.BackIconActivity;
import com.flapps.mobile.android.flapps.data.DataConnector;
import com.flapps.mobile.android.flapps.utils.Globals;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class DataTestActivity extends BackIconActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datatest); 
        
        
        FileInputStream in;
		try {
			//in = openFileInput("debug");
			//in = openFileInput(Globals.activitiesfile);
			in = openFileInput(Globals.datafile);
			//in = openFileInput(Globals.expensesfile);
			in = openFileInput( ApiConfig.FILENAME_CONFIG );
	        InputStreamReader inputStreamReader = new InputStreamReader(in);
	        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = bufferedReader.readLine()) != null) {
	            sb.append(line);
	        }
	        
	        TextView tv = (TextView) findViewById(R.id.text_data);
	        tv.setMovementMethod(new ScrollingMovementMethod());
	        tv.setText(sb.toString());
	        
	        //deleteFile(Globals.timerecordfile);
		} catch (Exception e) {
			TextView tv = (TextView) findViewById(R.id.text_data);
	        tv.setText("nofile");
			e.printStackTrace();
		}
        
//        return;
//        new DataConnector(this)
//			.execute("http://www.ivanbaca.com/todo/rest.php?command=getFullList");
	}
	
	public void showReturnedValue( String ret ){
        
        TextView tv = (TextView) findViewById(R.id.text_data);
        if ("".equals(ret)) ret = "Wrong Login";
        tv.setText(ret);
	}

}
