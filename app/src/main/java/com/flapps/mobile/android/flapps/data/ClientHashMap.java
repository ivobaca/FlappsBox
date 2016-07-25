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

public class ClientHashMap extends HashMap<Integer, ClientBean> {

	Comparator<ClientBean> comparator;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientHashMap( Document doc ) {
		NodeList nl = doc.getElementsByTagName("client");
		
		List<ClientBean> list = new ArrayList<ClientBean>();
		for ( int i=0; i< nl.getLength(); i++ ) {
			ClientBean cb = new ClientBean( );
			cb.load(nl.item(i));
			list.add(cb);
		}

		comparator = new Comparator<ClientBean>() {
		    public int compare(ClientBean c1, ClientBean c2) {
		        return c1.getName().compareTo(c2.getName()); // use your logic
		    }
		};

		Collections.sort(list, comparator);
		
		Iterator<ClientBean> i = list.iterator();
		//for ( int i=0; i< nl.getLength(); i++ ) {
		while ( i.hasNext() ) {
			ClientBean cb = i.next();
			this.put( cb.getId(), cb);
		}
	}

	public String[] getStringArray() {
		ArrayList<String> al = new ArrayList<String>();
		List<ClientBean> list = new ArrayList<ClientBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ClientBean> it = list.iterator();
	    while (it.hasNext()) {
	        ClientBean pairs = (ClientBean)it.next();
	        al.add(pairs.getName());
	    }
	    
	    return (String[]) al.toArray(new String[0]);
	}
	
	public ClientBean getPosition( int position ) {
		int counter=0;
		List<ClientBean> list = new ArrayList<ClientBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ClientBean> it = list.iterator();
	    while (it.hasNext()) {
	        ClientBean pairs = (ClientBean)it.next();
	        if (counter++==position) return pairs;
	    }
	    return new ClientBean();
	}
	
	public int getOrder (int id) {
		int counter=0;
		List<ClientBean> list = new ArrayList<ClientBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ClientBean> it = list.iterator();
	    while (it.hasNext()) {
	        ClientBean pairs = (ClientBean)it.next();
	        if (id==pairs.getId()) return counter;
	    	counter++;
	    }
	    return -1;
	}
	
	/*
	public ClientBean get( int id, int extId ) {
		if ( id>0 ) return get(id);
		List<ClientBean> list = new ArrayList<ClientBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ClientBean> it = list.iterator();
	    while (it.hasNext()) {
	    	ClientBean pairs = (ClientBean)it.next();
	        if (extId==pairs.getExtId()) return pairs;
	    }
	    return null;
	}
	*/
}
