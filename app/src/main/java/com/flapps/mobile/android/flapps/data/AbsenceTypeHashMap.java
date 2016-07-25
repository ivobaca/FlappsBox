package com.flapps.mobile.android.flapps.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class AbsenceTypeHashMap extends HashMap<Integer, AbsenceTypeBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbsenceTypeHashMap( Document doc ) {
		NodeList nl = doc.getElementsByTagName("absenceType");
		
		for ( int i=0; i< nl.getLength(); i++ ) {
			AbsenceTypeBean atb = new AbsenceTypeBean( );
			atb.load( nl.item(i) );
			this.put(atb.getId(), atb);
		}
	}

	public String[] getStringArray() {
		ArrayList<String> al = new ArrayList<String>();
		Iterator<Entry<Integer, AbsenceTypeBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer,AbsenceTypeBean> pairs = (Map.Entry<Integer,AbsenceTypeBean>)it.next();
	        al.add(pairs.getValue().getName());
	    }
	    
	    return (String[]) al.toArray(new String[0]);
	}
	
	public AbsenceTypeBean getPosition( int position ) {
		int counter=0;
		Iterator<Entry<Integer, AbsenceTypeBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer,AbsenceTypeBean> pairs = (Map.Entry<Integer,AbsenceTypeBean>)it.next();
	        if (counter++==position) return pairs.getValue();
	    }
	    return new AbsenceTypeBean();
	}


}
