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

import com.flapps.mobile.android.flapps.utils.Globals;


public class ProjectHashMap extends HashMap<Integer, ProjectBean> {

	Comparator<ProjectBean> comparator;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProjectHashMap( ) {
		super();
	}
	
		public ProjectHashMap( Document doc ) {
		NodeList nl = doc.getElementsByTagName("project");

		List<ProjectBean> list = new ArrayList<ProjectBean>();
		for ( int i=0; i< nl.getLength(); i++ ) {
			ProjectBean pb = new ProjectBean( );
			pb.load(nl.item(i));
			list.add(pb);
		}

		comparator = new Comparator<ProjectBean>() {
		    public int compare(ProjectBean c1, ProjectBean c2) {
		        return c1.getName().compareTo(c2.getName()); // use your logic
		    }
		};

		Collections.sort(list, comparator);
		
		Iterator<ProjectBean> i = list.iterator();
		//for ( int i=0; i< nl.getLength(); i++ ) {
		while ( i.hasNext() ) {
			ProjectBean pb = i.next();
			this.put( pb.getId(), pb);
		}
	}

	public ProjectHashMap( Document doc, int client ) {
		NodeList nl = doc.getElementsByTagName("project");

		List<ProjectBean> list = new ArrayList<ProjectBean>();
		for ( int i=0; i< nl.getLength(); i++ ) {
			ProjectBean pb = new ProjectBean( );
			pb.load(nl.item(i));
			list.add(pb);
		}

		comparator = new Comparator<ProjectBean>() {
		    public int compare(ProjectBean c1, ProjectBean c2) {
		        return c1.getName().compareTo(c2.getName()); // use your logic
		    }
		};
		
		Collections.sort(list, comparator);
		
		Iterator<ProjectBean> i = list.iterator();
		//for ( int i=0; i< nl.getLength(); i++ ) {
		while ( i.hasNext() ) {
			ProjectBean pb = i.next();
			if ( pb.getClientId() == client ) this.put( pb.getId(), pb);
		}
	}

	public String[] getStringArray() {
		ArrayList<String> al = new ArrayList<String>();
		List<ProjectBean> list = new ArrayList<ProjectBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ProjectBean> it = list.iterator();
	    while (it.hasNext()) {
	        ProjectBean pairs = (ProjectBean)it.next();
	        al.add(pairs.getName());
	    }

	    return (String[]) al.toArray(new String[0]);
	}
	
	public ProjectBean getPosition( int position ) {
		int counter=0;
		List<ProjectBean> list = new ArrayList<ProjectBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ProjectBean> it = list.iterator();
	    while (it.hasNext()) {
	        ProjectBean pairs = (ProjectBean)it.next();
	        if (counter++==position) return pairs;
	    }
	    return new ProjectBean();
	}
	
	public int getOrder (int id) {
		int counter=0;
		List<ProjectBean> list = new ArrayList<ProjectBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ProjectBean> it = list.iterator();
	    while (it.hasNext()) {
	        ProjectBean pairs = (ProjectBean)it.next();
	        if (id==pairs.getId()) return counter;
	    	counter++;
	    }
	    
	    return -1;
	}
	
	/*
	public ProjectBean get( int id, int extId ) {
		Log.v( "ProjectBean get", id+" - "+extId );
		if ( id>0 ) return get(id);
		Log.v( "ProjectBean get", "not get" );
		List<ProjectBean> list = new ArrayList<ProjectBean>( values() );

		Collections.sort( list, comparator );
		Iterator< ProjectBean> it = list.iterator();
	    while (it.hasNext()) {
	        ProjectBean pairs = (ProjectBean)it.next();
	        if (extId==pairs.getExtId()) return pairs;
	    }
	    return null;
	}
	*/
}

