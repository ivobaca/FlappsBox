package com.flapps.mobile.android.flapps.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class AbsenceRequestHashMap extends HashMap<Long, AbsenceRequestBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbsenceRequestHashMap( Document doc ) {
		NodeList nl = doc.getElementsByTagName("absenceRequest");
		
		for ( int i=0; i< nl.getLength(); i++ ) {
			AbsenceRequestBean ab = new AbsenceRequestBean( );
			ab.load( nl.item(i) );
			if ( ab.getExtId()==0 ) this.put(Long.valueOf(i), ab);
			else  this.put(ab.getExtId(), ab);
		}
	}
	
	public AbsenceRequestBean getPosition( int position ) {
		int counter=0;
		Iterator<Entry<Long, AbsenceRequestBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Long,AbsenceRequestBean> pairs = (Map.Entry<Long,AbsenceRequestBean>)it.next();
	        if (counter++==position) return pairs.getValue();
	    }
	    return new AbsenceRequestBean();
	}
	
	public ArrayList<AbsenceRequestBean> getAbsenceRequestBeanList() {
		ArrayList<AbsenceRequestBean> al = new ArrayList<AbsenceRequestBean>();
		Iterator<Entry<Long, AbsenceRequestBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Long,AbsenceRequestBean> pairs = (Map.Entry<Long,AbsenceRequestBean>)it.next();
	        al.add(pairs.getValue());
	    }
	    
	    Comparator<AbsenceRequestBean> comparator = new Comparator<AbsenceRequestBean>() {
		    public int compare(AbsenceRequestBean c1, AbsenceRequestBean c2) {
		        return c1.compareTo(c2); // use your logic
		    }
		};
		
		Collections.sort(al, comparator);
	    
	    return al;
	}

}
