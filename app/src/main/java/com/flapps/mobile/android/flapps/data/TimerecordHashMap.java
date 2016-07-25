package com.flapps.mobile.android.flapps.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class TimerecordHashMap extends HashMap<Long, TimerecordBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimerecordHashMap( Document doc ) {
		NodeList nl = doc.getElementsByTagName("timerecord");
		
		for ( int i=0; i< nl.getLength(); i++ ) {
			TimerecordBean pb = new TimerecordBean( );
			pb.load( nl.item(i) );
			if (pb.getId() > 0)
				this.put(pb.getId(), pb);
			else this.put(pb.getExtId(), pb);
		}
	}

	public TimerecordHashMap( Document doc, Calendar c ) {
		int i;
		NodeList nl = null;
		NodeList ds = doc.getElementsByTagName("dailySheet");
		for ( i=0; i< ds.getLength(); i++ ) {
			NamedNodeMap attrs = ds.item(i).getAttributes();
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd"); 
			if ( formater.format(c.getTime()).equals(attrs.getNamedItem("date").getNodeValue() ) ) {
				nl = ((Element) ds.item(i)).getElementsByTagName("timerecord");
			}
		}
		
		if ( nl != null) {
			for ( i=0; i< nl.getLength(); i++ ) {
				TimerecordBean pb = new TimerecordBean( );
				pb.load( nl.item(i) );
				if ( !pb.isAbsence() ) this.put(pb.getId(), pb);
			}
		}
		
	}

	public TimerecordBean[] getTimerecordBeanArray() {
	    
	    return (TimerecordBean[]) getTimerecordBeanList().toArray(new TimerecordBean[0]);
	}
	
	public ArrayList<TimerecordBean> getTimerecordBeanList() {
		ArrayList<TimerecordBean> al = new ArrayList<TimerecordBean>();
		Iterator<Entry<Long, TimerecordBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Long,TimerecordBean> pairs = (Map.Entry<Long,TimerecordBean>)it.next();
	        al.add(pairs.getValue());
	    }
	    
	    return al;
	}
	
	public TimerecordBean getTimerecordFromExtId( long extId ) {
		Iterator<Entry<Long, TimerecordBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Long,TimerecordBean> pairs = (Map.Entry<Long,TimerecordBean>)it.next();
	        TimerecordBean item = pairs.getValue();
	        if ( item.getExtId() == extId ) return item;
	    }
	    return null;
	    
	}
/*	
	public ProjectBean getPosition( int position ) {
		int counter=0;
		Iterator<Entry<Integer, ProjectBean>> it = entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer,ProjectBean> pairs = (Map.Entry<Integer,ProjectBean>)it.next();
	        if (counter++==position) return pairs.getValue();
	    }
	    return new ProjectBean();
	}
*/
}
