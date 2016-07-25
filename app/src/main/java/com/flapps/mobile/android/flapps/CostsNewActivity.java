package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.flapps.mobile.android.flapps.data.ClientHashMap;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.ExpenseBean;
import com.flapps.mobile.android.flapps.data.ExpenseHashMap;
import com.flapps.mobile.android.flapps.data.ExpenseTypeHashMap;
import com.flapps.mobile.android.flapps.data.ProjectHashMap;

public class CostsNewActivity extends BackIconActivity implements
		OnItemSelectedListener {

	public final static int RETURN_CODE_ACTIVITY = 0;
	public final static int RETURN_CODE_PROJECT = 1;
	public final static int RETURN_CODE_DESCRIPTION = 2;

	public ExpenseBean eb;
	private Menu mainmenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_costsnew);

		Bundle b = getIntent().getExtras();
		final long value = b.getLong("id");

		ExpenseTypeHashMap ethm = new DataModel(CostsNewActivity.this)
				.getExpenceTypes();

		Spinner spinner = (Spinner) findViewById(R.id.costs_type);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_item,
				ethm.getStringArray());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		if (value == 0) {
			eb = new ExpenseBean();
			eb.setTypeId(ethm.getPosition(0).getId());
			eb.setType(ethm.getPosition(0).getName());
		} else {
			ExpenseHashMap ehm = new DataModel(CostsNewActivity.this)
					.getExpences();
			eb = ehm.get(Long.valueOf(value));
			Log.v("Type", "" + ethm.getOrder(eb.getType()));
			spinner.setSelection(ethm.getOrder(eb.getType()));

			ClientHashMap chm = new DataModel(CostsNewActivity.this)
					.getClients();
			ProjectHashMap phm = new DataModel(CostsNewActivity.this)
					.getProjects();

			EditText et1 = (EditText) findViewById(R.id.costs_client);
			EditText et2 = (EditText) findViewById(R.id.costs_project);

			et1.setText(chm.get(eb.getClientId()).getName());
			et2.setText(phm.get(eb.getProjectId()).getName());

			EditText et = (EditText) findViewById(R.id.costs_date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			et.setText(sdf.format(eb.getDate().getTime()));

			EditText eta = (EditText) findViewById(R.id.costs_amount);
			eta.setText("" + eb.getAmount());

			EditText etn = (EditText) findViewById(R.id.costs_note);
			etn.setText(eb.getNote());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.costsnew, menu);
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
		case R.id.action_send:
			try {
				EditText eta = (EditText) findViewById(R.id.costs_amount);
				eb.setAmount(Double.parseDouble(eta.getText().toString()));
			} catch (Exception e) {
				eb.setAmount(0);
			}
			EditText etn = (EditText) findViewById(R.id.costs_note);
			eb.setNote(etn.getText().toString());

			if (eb.check()) {
				if ( eb.getId() == 0 && eb.getExtId() == 0 ) eb.setExtId( Math.round ( 1000000 + ( Math.random() * 500000 ) ) ); 
				try {
					Log.i("Save Expenses", eb.getClient());
					eb.save(CostsNewActivity.this);
					finish();
				} catch (Exception e) {
					Toast.makeText(
							CostsNewActivity.this,
							getResources().getString(
									R.string.activity_costsnew_save_failed),
							Toast.LENGTH_LONG).show();
				}
			}
			else
				Toast.makeText(
						CostsNewActivity.this,
						getResources().getString(
								R.string.activity_costsnew_empty_fields),
						Toast.LENGTH_LONG).show();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClickDate(View view) {
		Calendar c = Calendar.getInstance();

		DatePickerDialog DPD;
		DPD = new DatePickerDialog(CostsNewActivity.this, mDateSetListener,
				c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		DPD.show();
	}

	public void onClickProject(View view) {
		Intent i1 = new Intent(CostsNewActivity.this, ClientsActivity.class);
		startActivityForResult(i1, RETURN_CODE_PROJECT);
	}

	/*
	public void afterSaveFailed(String ret) {
		deleteFile(Globals.expensesfile);
		Toast.makeText(CostsNewActivity.this, "Failed", Toast.LENGTH_LONG)
				.show();

	}

	public void afterSave(String ret) {
		if ("OK".equals(ret))
			Toast.makeText(CostsNewActivity.this, "Saved", Toast.LENGTH_LONG)
					.show();
		else
			Toast.makeText(CostsNewActivity.this, "Failed: " + ret,
					Toast.LENGTH_LONG).show();

		deleteFile(Globals.expensesfile);
		new DataConnector(CostsNewActivity.this).execute("reloadExpenses", "");

	}

	public void afterReload(String result) {
		DataModel dm = new DataModel(this);
		try {
			dm.saveFile(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}
	*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RETURN_CODE_PROJECT:
			eb.setClientId(data.getIntExtra("client", 0));
			eb.setProjectId(data.getIntExtra("id", 0));

			ClientHashMap chm = new DataModel(CostsNewActivity.this)
					.getClients();
			ProjectHashMap phm = new DataModel(CostsNewActivity.this)
					.getProjects();

			EditText et1 = (EditText) findViewById(R.id.costs_client);
			EditText et2 = (EditText) findViewById(R.id.costs_project);

			if (data.getIntExtra("client", 0) != 0
					&& data.getIntExtra("id", 0) != 0) {
				et1.setText(chm.get(data.getIntExtra("client", 0)).getName());
				eb.setClient(chm.get(data.getIntExtra("client", 0)).getName());
				et2.setText(phm.get(data.getIntExtra("id", 0)).getName());
				eb.setProject(phm.get(data.getIntExtra("id", 0)).getName());
			}

			break;
		}
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);

			eb.setDate(c);

			EditText et = (EditText) findViewById(R.id.costs_date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			et.setText(sdf.format(c.getTime()));
		}
	};

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		ExpenseTypeHashMap ethm = new DataModel(CostsNewActivity.this)
				.getExpenceTypes();
		eb.setTypeId(ethm.getPosition(arg2).getId());
		eb.setType(ethm.getPosition(arg2).getName());

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
