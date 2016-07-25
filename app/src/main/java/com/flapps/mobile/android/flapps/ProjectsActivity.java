package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import com.flapps.mobile.android.flapps.adapters.MainListAdapter;
import com.flapps.mobile.android.flapps.data.ClientBean;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.ProjectBean;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProjectsActivity extends BackIconActivity {

	public Intent resultIntent;
	private Menu mainmenu;
	ClientBean client = null;
	private int clientId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projects);

		resultIntent = new Intent();
		resultIntent.putExtra("id", Globals.timerecord.getProjectId());
		setResult(RESULT_OK, resultIntent);

		Bundle b = getIntent().getExtras();
		final int value = b.getInt("id");
		client = new DataModel(ProjectsActivity.this).getClients().get(value);
		if (client != null)
			setTitle(client.getName());
		clientId = value;
	}

	@Override
	protected void onResume() {

		ListView lv = (ListView) findViewById(R.id.projects_list);
		String[] projects = new DataModel(this).getProjects(clientId)
				.getStringArray();
		int checked = new DataModel(ProjectsActivity.this)
				.getProjects(clientId).getOrder(
						Globals.timerecord.getProjectId());
		ArrayAdapter<String> aa = new MainListAdapter(this,
				R.layout.activity_list_item_billable, projects, checked);
		lv.setAdapter(aa);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ProjectsActivity.this.resultIntent.putExtra(
						"id",
						new DataModel(ProjectsActivity.this)
								.getProjects(clientId).getPosition(position)
								.getId());
				ProjectsActivity.this.resultIntent.putExtra("clientId",
						clientId);
				ProjectsActivity.this.setResult(RESULT_OK, resultIntent);
				ProjectsActivity.this.finish();
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						ProjectsNewActivity.class);
				intent.putExtra("id", new DataModel(ProjectsActivity.this)
						.getProjects(clientId).getPosition(position).getId());
				startActivity(intent);

				return true;
			}
		});

		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.projects, menu);
		mainmenu = menu;

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:

			Intent intent = new Intent(getApplicationContext(),
					ProjectsNewActivity.class);
			intent.putExtra("id", 0);
			intent.putExtra("clientId", client.getId());
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
