package com.flapps.mobile.android.flapps.data;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ExpenseTypeBean {
	
	int id=0;
	String name="";
	boolean active=true;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	//	actions
	public void load( Node expense ) {
		NamedNodeMap attrs = expense.getAttributes();
		
		if ( attrs.getNamedItem("id") != null ) setId(Integer.parseInt(attrs.getNamedItem("id").getNodeValue()));
		if ( attrs.getNamedItem("name") != null ) setName(attrs.getNamedItem("name").getNodeValue());
		if ( attrs.getNamedItem("active") != null ) setActive( ("true".equals(attrs.getNamedItem("active").getNodeValue()) )?true:false );
	}


}
