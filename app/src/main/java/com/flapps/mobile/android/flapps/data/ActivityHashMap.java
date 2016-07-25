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

import android.widget.ArrayAdapter;

public class ActivityHashMap extends HashMap<Integer, ActivityBean> {

	Comparator<ActivityBean> comparator;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActivityHashMap( Document doc ) {
		NodeList nl = doc.getElementsByTagName("activity");
		
		List<ActivityBean> list = new ArrayList<ActivityBean>();
		for ( int i=0; i< nl.getLength(); i++ ) {
			ActivityBean pb = new ActivityBean( );
			pb.load(nl.item(i));
			if ( !pb.isAbsence() )
				list.add(pb);
		}

		comparator = new Comparator<ActivityBean>() {
		    public int compare(ActivityBean c1, ActivityBean c2) {
		        return c1.getName().compareTo(c2.getName()); // use your logic
		    }
		};

		Collections.sort(list, comparator);
		
		Iterator<ActivityBean> i = list.iterator();
		//for ( int i=0; i< nl.getLength(); i++ ) {
		while ( i.hasNext() ) {
			ActivityBean pb = i.next();
			this.put( pb.getId(), pb);
		}
	}
	
	public String[] getStringArray() {
		ArrayList<String> al = new ArrayList<String>();
		List<ActivityBean> list = new ArrayList<ActivityBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ActivityBean> it = list.iterator();
	    while (it.hasNext()) {
	        ActivityBean pairs = (ActivityBean)it.next();
	        al.add(pairs.getName());
	    }

	    return (String[]) al.toArray(new String[0]);
	}
	
	public ActivityBean getPosition( int position ) {
		int counter=0;
		List<ActivityBean> list = new ArrayList<ActivityBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ActivityBean> it = list.iterator();
	    while (it.hasNext()) {
	        ActivityBean pairs = (ActivityBean)it.next();
	        if (counter++==position) return pairs;
	    }
	    return new ActivityBean();
	}
	
	public int getOrder (int id) {
		int counter=0;
		List<ActivityBean> list = new ArrayList<ActivityBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ActivityBean> it = list.iterator();
	    while (it.hasNext()) {
	        ActivityBean pairs = (ActivityBean)it.next();
	        if (id==pairs.getId()) return counter;
	    	counter++;
	    }
	    return -1;
	}
	
}
