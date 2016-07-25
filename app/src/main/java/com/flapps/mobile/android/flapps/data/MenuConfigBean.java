package com.flapps.mobile.android.flapps.data;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.simple.JSONArray;

public class MenuConfigBean implements Serializable {

	protected ArrayList<String> items;
	
	public MenuConfigBean( JSONArray items ) {
		
		this.items = new ArrayList<String>();
		for(int i=0; i< items.size(); i++ ) {
			
			this.items.add( (String)items.get(i) );
		}
	}

	public MenuConfigBean() {
		items = new ArrayList<String>();
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public void setItems(ArrayList<String> items) {
		this.items = items;
	}
	
	
}
