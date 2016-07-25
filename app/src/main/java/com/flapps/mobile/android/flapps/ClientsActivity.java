package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import com.flapps.mobile.android.flapps.adapters.MainListAdapter;
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

public class ClientsActivity extends BackIconActivity {

	public Intent resultIntent;
	private Menu mainmenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        
        resultIntent = new Intent();
        resultIntent.putExtra("id", Globals.timerecord.getProjectId());
        setResult(RESULT_OK, resultIntent);
        
	}

	@Override
	protected void onResume() {
        ListView lv = (ListView) findViewById(R.id.clients_list);
        String[] clients = new DataModel(this).getClients().getStringArray();
        ProjectBean pb = new DataModel(ClientsActivity.this).getProjects().get(Globals.timerecord.getProjectId());
        int client = -1;
        if (pb != null) client = new DataModel(ClientsActivity.this).getProjects().get(Globals.timerecord.getProjectId()).getClientId();
        int checked = new DataModel(ClientsActivity.this).getClients().getOrder(client);
        ArrayAdapter<String> aa = new MainListAdapter(this, R.layout.activity_list_item_billable, clients, checked);
        lv.setAdapter(aa);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	Intent intent = new Intent(ClientsActivity.this, ProjectsActivity.class);
            	Bundle b = new Bundle();
            	b.putInt("id", new DataModel(ClientsActivity.this).getClients().getPosition(position).getId()); //Your id
            	intent.putExtras(b); //Put your id to your next Intent
            	startActivityForResult(intent, 0);
            }
        });
        
        
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int position, long id) {
            	Intent intent = new Intent(getApplicationContext(),   
    	     			ClientsNewActivity.class);  
    	     	intent.putExtra("id", new DataModel(ClientsActivity.this).getClients().getPosition(position).getId());
    	     	startActivity(intent); 
    	     	
                return true;
            }
        }); 
		
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		ClientsActivity.this.resultIntent.putExtra("id", data.getIntExtra("id", 0) );
		ClientsActivity.this.resultIntent.putExtra("clientId", data.getIntExtra("clientId", 0) );
		ClientsActivity.this.setResult(RESULT_OK, resultIntent);
		ClientsActivity.this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.clients, menu);
		mainmenu = menu;
		
		return true;
	}
	
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item)
	 {
		switch (item.getItemId())
	     {
	     case R.id.action_new:
	     	
	     	Intent intent = new Intent(getApplicationContext(),   
	     			ClientsNewActivity.class);  
	     	intent.putExtra("id", 0);
	     	startActivity(intent); 
	     	
	        return true;
	
	
	     default:
	         return super.onOptionsItemSelected(item);
	     }
	}


}
