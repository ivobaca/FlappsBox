package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.flapps.mobile.android.flapps.adapters.AbsenceRequestListAdapter;
import com.flapps.mobile.android.flapps.data.AbsenceRequestBean;
import com.flapps.mobile.android.flapps.data.DataModel;

public class AbsenceActivity extends DrawerActivity {

	private Menu mainmenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		drawer_item = 2;
		setContentView(R.layout.activity_absence);
		super.onCreate(savedInstanceState);
		setTitle(R.string.activity_absencerequest_title);

		ListView lv = (ListView) findViewById(R.id.absencerequest_list);
		ArrayList<AbsenceRequestBean> absencerequests = new DataModel(this)
				.getAbsenceRequests().getAbsenceRequestBeanList();
		AbsenceRequestListAdapter aa = new AbsenceRequestListAdapter(this,
				R.layout.absencerequest_list_item, absencerequests);
		lv.setAdapter(aa);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}

	@Override
	protected void onResume() {
		
		ListView lv = (ListView) findViewById(R.id.absencerequest_list);
		ArrayList<AbsenceRequestBean> absencerequests = new DataModel(this)
				.getAbsenceRequests().getAbsenceRequestBeanList();
		AbsenceRequestListAdapter aa = new AbsenceRequestListAdapter(this,
				R.layout.absencerequest_list_item, absencerequests);
		lv.setAdapter(aa);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});


		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//	Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.absencerequest, menu);
		this.mainmenu = menu;
		
		return true;
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
		case R.id.action_new:

			Intent intent = new Intent(getApplicationContext(),
					AbsenceNewActivity.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
