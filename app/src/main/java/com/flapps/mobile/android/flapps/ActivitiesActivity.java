package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import com.flapps.mobile.android.flapps.adapters.MainListAdapter;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ActivitiesActivity extends BackIconActivity {

	public Intent resultIntent;
	private Menu mainmenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activities);

		resultIntent = new Intent();
		resultIntent.putExtra("id", Globals.timerecord.getActivityId());
		resultIntent.putExtra("billable", Globals.timerecord.isBillable());
		setResult(RESULT_OK, resultIntent);

		ListView lvb = (ListView) findViewById(R.id.activities_billable_list);
		String[] mActivityFieldTitles = getResources().getStringArray(
				R.array.activities_fields_array_billable);
		int billable = -1;
		if (Globals.timerecord.isBillable())
			billable = 0;
		ArrayAdapter<String> aab = new MainListAdapter(this,
				R.layout.activity_list_item_billable, mActivityFieldTitles,
				billable);
		lvb.setAdapter(aab);
		lvb.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageView iv = (ImageView) findViewById(R.id.activities_edit);
				if (iv != null) {
					if (iv.getVisibility() == View.VISIBLE) {
						iv.setVisibility(View.INVISIBLE);
						resultIntent.putExtra("billable", false);
					} else {
						iv.setVisibility(View.VISIBLE);
						resultIntent.putExtra("billable", true);
					}
				}
				// ActivitiesActivity.this.resultIntent.putExtra("id", new
				// DataModel(ActivitiesActivity.this).getActivities().getPosition(position).getId());
				// ActivitiesActivity.this.setResult(RESULT_OK, resultIntent);
				// ActivitiesActivity.this.finish();
			}
		});

	}

	@Override
	protected void onResume() {
		ListView lv = (ListView) findViewById(R.id.activities_list);
		String[] activities = new DataModel(this).getActivities()
				.getStringArray();
		int checked = new DataModel(ActivitiesActivity.this).getActivities()
				.getOrder(Globals.timerecord.getActivityId());
		Log.v("cjeked", "" + checked);
		ArrayAdapter<String> aa = new MainListAdapter(this,
				R.layout.activity_list_item_billable, activities, checked);
		lv.setAdapter(aa);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ActivitiesActivity.this.resultIntent.putExtra("id",
						new DataModel(ActivitiesActivity.this).getActivities()
								.getPosition(position).getId());
				ActivitiesActivity.this.setResult(RESULT_OK, resultIntent);
				ActivitiesActivity.this.finish();
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						ActivitiesNewActivity.class);
				intent.putExtra("id", new DataModel(ActivitiesActivity.this)
						.getActivities().getPosition(position).getId());
				startActivity(intent);

				return true;
			}
		});

		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activities, menu);
		mainmenu = menu;

		return true;
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:

			Intent intent = new Intent(getApplicationContext(),
					ActivitiesNewActivity.class);
			intent.putExtra("id", 0);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
