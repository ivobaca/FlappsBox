package com.flapps.mobile.android.flapps.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ExpenseHashMap extends HashMap<Long, ExpenseBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpenseHashMap( Document doc ) {
		NodeList nl = doc.getElementsByTagName("expense");
		
		for ( int i=0; i< nl.getLength(); i++ ) {
			ExpenseBean ab = new ExpenseBean( );
			ab.load( nl.item(i) );
			if ( ab.getId() > 0) this.put(ab.getId(), ab);
			else this.put(ab.getExtId(), ab);
		}
	}
	
	public ExpenseBean getPosition( int position ) {
		int counter=0;
		Iterator<Entry<Long, ExpenseBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Long,ExpenseBean> pairs = (Map.Entry<Long,ExpenseBean>)it.next();
	        if (counter++==position) return pairs.getValue();
	    }
	    return new ExpenseBean();
	}
	
	public ArrayList<ExpenseBean> getExpenseBeanList() {
		ArrayList<ExpenseBean> al = new ArrayList<ExpenseBean>();
		Iterator<Entry<Long, ExpenseBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Long,ExpenseBean> pairs = (Map.Entry<Long,ExpenseBean>)it.next();
	        al.add(pairs.getValue());
	    }
	    
	    return al;
	}

}
