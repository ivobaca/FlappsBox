package com.flapps.mobile.android.flapps;

import android.os.Bundle;
import android.view.MenuItem;

public class BackIconActivity extends BasicActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:

	            this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
    }

}
