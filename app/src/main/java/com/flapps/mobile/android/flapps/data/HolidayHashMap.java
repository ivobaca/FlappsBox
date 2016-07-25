package com.flapps.mobile.android.flapps.data;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class HolidayHashMap extends HashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HolidayHashMap( Document doc ) {
		NodeList nl = doc.getElementsByTagName("holiday");
		
		for ( int i=0; i< nl.getLength(); i++ ) {
			NamedNodeMap nnm = nl.item(i).getAttributes();
			this.put(nnm.getNamedItem("date").getNodeValue(), nnm.getNamedItem("name").getNodeValue());
		}
	}
}
