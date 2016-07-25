package com.flapps.mobile.android.flapps.data;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AbsenceTypeBean {

	public int id=0;
	public String name="";
	public String shortName="";
	public String description="";
	public boolean workedTime=false;
	public String unit="";
	
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
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isWorkedTime() {
		return workedTime;
	}
	public void setWorkedTime(boolean workedTime) {
		this.workedTime = workedTime;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	//	actions
	public void load( Node activity ) {
		NamedNodeMap attrs = activity.getAttributes();
		
		if ( attrs.getNamedItem("id") != null ) setId(Integer.parseInt(attrs.getNamedItem("id").getNodeValue()));
		if ( attrs.getNamedItem("name") != null ) setName(attrs.getNamedItem("name").getNodeValue());
		if (attrs.getNamedItem("shortName") != null	)
			setShortName(attrs.getNamedItem("shortName").getNodeValue());
		if ( attrs.getNamedItem("workedTime") != null ) setWorkedTime( ("true".equals(attrs.getNamedItem("workedTime").getNodeValue()) )?true:false );
		if ( attrs.getNamedItem("description") != null ) setDescription(attrs.getNamedItem("description").getNodeValue());
		if ( attrs.getNamedItem("unit") != null ) setUnit(attrs.getNamedItem("unit").getNodeValue());
	}
	
}
