package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import java.util.ArrayList;

import com.flapps.mobile.android.flapps.adapters.CostsListAdapter;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.ExpenseBean;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CostsActivity extends DrawerActivity {
	
	   private Menu mainmenu;

	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		drawer_item = 2;
		setContentView(R.layout.activity_costs);
		super.onCreate(savedInstanceState);
        setTitle(R.string.activity_costs_title);
        
        ListView lv = (ListView) findViewById(R.id.costs_list);
        final ArrayList<ExpenseBean> costs = new DataModel(this).getExpences().getExpenseBeanList();
        CostsListAdapter aa = new CostsListAdapter(this, R.layout.costs_list_item, costs);
        lv.setAdapter(aa);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	Log.v("List", "clicked");
            	ExpenseBean eb = costs.get(position);
            	Intent intent = new Intent(CostsActivity.this.getApplicationContext(),   
   	                 CostsNewActivity.class);  
            	intent.putExtra("id", eb.getId());
   	     		startActivity(intent); 
            }
        });

	}

	@Override
	protected void onResume() {
		
	       ListView lv = (ListView) findViewById(R.id.costs_list);
	        final ArrayList<ExpenseBean> costs = new DataModel(this).getExpences().getExpenseBeanList();
	        CostsListAdapter aa = new CostsListAdapter(this, R.layout.costs_list_item, costs);
	        lv.setAdapter(aa);
	        lv.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	            	Log.v("List", "clicked");
	            	ExpenseBean eb = costs.get(position);
	            	Intent intent = new Intent(CostsActivity.this.getApplicationContext(),   
	   	                 CostsNewActivity.class);  
	            	if (eb.getId()>0)
	            		intent.putExtra("id", eb.getId());
	            	else intent.putExtra("id", eb.getExtId());
	   	     		startActivity(intent); 
	            }
	        });


		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.costs, menu);
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
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}

			switch (item.getItemId())
	     {
	     case R.id.action_new:
	     	
	     	Intent intent = new Intent(getApplicationContext(),   
	                 CostsNewActivity.class);  
	     	intent.putExtra("id", 0);
	     	startActivity(intent); 
	         return true;
	
	
	     default:
	         return super.onOptionsItemSelected(item);
	     }
	}  
}
