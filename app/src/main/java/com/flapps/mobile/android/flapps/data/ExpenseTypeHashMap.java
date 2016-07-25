package com.flapps.mobile.android.flapps.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.util.Log;

public class ExpenseTypeHashMap extends HashMap<Integer, ExpenseTypeBean> {

	Comparator<ExpenseTypeBean> comparator;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpenseTypeHashMap( Document doc ) {
		NodeList nl = doc.getElementsByTagName("expenseType");
		
		for ( int i=0; i< nl.getLength(); i++ ) {
			ExpenseTypeBean atb = new ExpenseTypeBean( );
			atb.load( nl.item(i) );
			this.put(atb.getId(), atb);
		}
		
		comparator = new Comparator<ExpenseTypeBean>() {
		    public int compare(ExpenseTypeBean c1, ExpenseTypeBean c2) {
		        return c1.getName().compareTo(c2.getName()); // use your logic
		    }
		};


	}
	
	public String[] getStringArray() {
		ArrayList<String> al = new ArrayList<String>();
		Iterator<Entry<Integer, ExpenseTypeBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer,ExpenseTypeBean> pairs = (Map.Entry<Integer,ExpenseTypeBean>)it.next();
	        al.add(pairs.getValue().getName());
	    }
	    
	    return (String[]) al.toArray(new String[0]);
	}
	
	public ExpenseTypeBean getPosition( int position ) {
		int counter=0;
		Iterator<Entry<Integer, ExpenseTypeBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer,ExpenseTypeBean> pairs = (Map.Entry<Integer,ExpenseTypeBean>)it.next();
	        if (counter++==position) return pairs.getValue();
	    }
	    return new ExpenseTypeBean();
	}

	public int getOrder (int id) {
		Log.v("Order", ""+id);
		int counter=0;
		List<ExpenseTypeBean> list = new ArrayList<ExpenseTypeBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ExpenseTypeBean> it = list.iterator();
	    while (it.hasNext()) {
	    	ExpenseTypeBean pairs = (ExpenseTypeBean)it.next();
			Log.v("Order", "x: "+pairs.getId());
	        if (id==pairs.getId()) return counter;
	    	counter++;
	    }
	    
	    return -1;
	}

	public int getOrder (String name) {
		Log.v("Order", ""+name);
		int counter=0;
		List<ExpenseTypeBean> list = new ArrayList<ExpenseTypeBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ExpenseTypeBean> it = list.iterator();
	    while (it.hasNext()) {
	    	ExpenseTypeBean pairs = (ExpenseTypeBean)it.next();
			Log.v("Order", "x: "+pairs.getName());
	        if (name.equals( pairs.getName() ) ) return counter;
	    	counter++;
	    }
	    
	    return -1;
	}

}
