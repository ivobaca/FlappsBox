package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flapps.mobile.android.flapps.data.AppStatus;
import com.flapps.mobile.android.flapps.data.DataConnector;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.DataSync;
import com.flapps.mobile.android.flapps.utils.Globals;

public class LoginActivity extends BasicActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().hide();
		AppStatus as = new AppStatus(LoginActivity.this);
		as.setShowIntro("shown");

	}
	
	public void login(View v) {
		EditText te = (EditText) findViewById(R.id.login_domain);
		Globals.domain = te.getText().toString();

		te = (EditText) findViewById(R.id.login_login);
		Globals.login = te.getText().toString();

		te = (EditText) findViewById(R.id.login_pass);
		Globals.password = te.getText().toString();
		
		AppStatus as = new AppStatus(LoginActivity.this);
		as.setCredentials(Globals.domain, Globals.login, Globals.password);

		//new DataConnector(LoginActivity.this).execute("login", "");
		DataSync ds = new DataSync( LoginActivity.this );
		ds.actionTag = "login";
		ds.execute("login", "");
	}
	
	public void logged(String s) {
		
		if ( "".equals(s.trim())) {
			Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
		} else {
			//DataModel dm = new DataModel(LoginActivity.this);
			try {
				DataModel.saveFile( LoginActivity.this, s );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
		}
	}
	
	public void toAccount(View v) {
		Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
		startActivity(intent);
		LoginActivity.this.finish();
	}
}
