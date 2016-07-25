package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flapps.mobile.android.flapps.config.DrawerMenu;
import com.flapps.mobile.android.flapps.test.DataTestActivity;
import com.flapps.mobile.android.flapps.utils.Globals;

import org.apache.commons.io.FileUtils;

import java.io.IOException;

public class DrawerActivity extends BasicActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	protected ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mFieldTitles;
	
	protected int drawer_item = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mTitle = mDrawerTitle = getResources().getString(R.string.activity_main_title);
		mFieldTitles = getResources().getStringArray(R.array.fields_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
//		mDrawerList.setAdapter(new ArrayAdapter(this,
//				R.layout.drawer_list_item, mFieldTitles));
		mDrawerList.setAdapter(new ArrayAdapter(this,
				R.layout.drawer_list_item, DrawerMenu.getArray(DrawerActivity.this)));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(0xff173569));

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			// selectItem(0);
		}

		String[] mMainFieldTitles = getResources().getStringArray(
				R.array.main_fields_array);

	}
	
	


	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {

		if (position == drawer_item) {
			mDrawerLayout.closeDrawers();
			return;
		}
		
		int mapped_position = DrawerMenu.getMap(DrawerActivity.this)[position];
		switch (mapped_position) {
		case 0:
			if (drawer_item!=0) finish();
			overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_right_out);

			break;
		case 1:
			Intent intent = new Intent(getApplicationContext(),
					CostsActivity.class);
			startActivity(intent);
			if (drawer_item!=0) finish();
			overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_left_out);

			break;
		case 2:
			Intent intent2 = new Intent(getApplicationContext(),
					AbsenceActivity.class);
			startActivity(intent2);
			if (drawer_item!=0) finish();
			overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_left_out);

			break;
		case 3:
			Intent intent6 = new Intent(getApplicationContext(),
					ComeInOutActivity.class);
			startActivity(intent6);
			if (drawer_item!=0) finish();
			overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_left_out);

			break;
		case 4:
			if (drawer_item!=3) finish();
			overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_right_out);

			break;
		case 5:
			Intent intent5 = new Intent(getApplicationContext(),
					SyncActivity.class);
			startActivity(intent5);
			if (drawer_item!=0) finish();
			overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_left_out);

			// new DataConnector(MainActivity.this).execute("reload", "");
			break;
		case 6:
			Intent intent3 = new Intent(getApplicationContext(),
					AboutActivity.class);
			startActivity(intent3);
			if (drawer_item!=0) finish();
			overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_left_out);


			break;
		case 7:
			deleteFile(Globals.datafile);
			deleteFile(Globals.statusfile);
			deleteFile(Globals.expensesfile);
			deleteFile(Globals.absencesfile);
			try {
				FileUtils.cleanDirectory( this.getFilesDir() );
			} catch (IOException e) {
				e.printStackTrace();
			}
			Globals.exit = true;
			finish();
			break;
		case 8:
			Intent intent4 = new Intent(getApplicationContext(),
					DataTestActivity.class);
			startActivity(intent4);
			if (drawer_item!=0) finish();
			overridePendingTransition(R.anim.swipe_right_in, R.anim.swipe_left_out);

			break;
		}
		// update the main content by replacing fragments
		// TextView tv = (TextView) findViewById(R.id.text_view);
		String[] fields = getResources().getStringArray(R.array.fields_array);
		// tv.setText((CharSequence) fields[position]);

		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
