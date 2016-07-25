package com.flapps.mobile.android.flapps.data;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.json.simple.JSONObject;

import android.content.Context;


public class ActivityBean implements Comparable<ActivityBean> {
	public int id = 0;
	public String name = "";
	public String note="";
	public boolean workedTime=true;
	public String project="allowed";
	public boolean absence=false;
	public boolean active=true;
	
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isWorkedTime() {
		return workedTime;
	}
	public void setWorkedTime(boolean workedTime) {
		this.workedTime = workedTime;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public boolean isAbsence() {
		return absence;
	}
	public void setAbsence(boolean absence) {
		this.absence = absence;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	//	actions
	
	public void save( Context cx ) throws TransformerConfigurationException, IOException, ParserConfigurationException, SAXException, TransformerException {
		
		DataModel dm = new DataModel(cx);
		dm.storeActivity(this);
	}

	public void load( Node activity ) {
		NamedNodeMap attrs = activity.getAttributes();
		
		if ( attrs.getNamedItem("id") != null ) setId(Integer.parseInt(attrs.getNamedItem("id").getNodeValue()));
		if ( attrs.getNamedItem("name") != null ) setName(attrs.getNamedItem("name").getNodeValue());
		Node n = attrs.getNamedItem("note");
		if (n!=null) setNote(attrs.getNamedItem("note").getNodeValue());
		if ( attrs.getNamedItem("workedTime") != null ) setWorkedTime( ("1".equals(attrs.getNamedItem("workedTime").getNodeValue()) )?true:false );
		if ( attrs.getNamedItem("project") != null ) setProject(attrs.getNamedItem("project").getNodeValue());
		if ( attrs.getNamedItem("absence") != null ) setAbsence( ("true".equals(attrs.getNamedItem("absence").getNodeValue()) )?true:false );
		if ( attrs.getNamedItem("active") != null ) setActive( ("1".equals(attrs.getNamedItem("active").getNodeValue()) )?true:false );
		if ( attrs.getNamedItem("extId") != null ) setId(Integer.parseInt(attrs.getNamedItem("extId").getNodeValue()));
	}
	
	@Override
	public int compareTo(ActivityBean another) {
		return another.getName().compareTo(this.getName());
	}
	
	public Element createElement( Document doc ) {
		Element activity = doc.createElement("activity");
		
		((Element) activity).setAttribute("id", ""+getId());
		((Element) activity).setAttribute("name", ""+getName());
		
		((Element) activity).setAttribute("workedTime", ""+(isWorkedTime()?"1":"0"));
		((Element) activity).setAttribute("project", ""+getProject());
		((Element) activity).setAttribute("absence", ""+(isAbsence()?"1":"0"));
		((Element) activity).setAttribute("active", ""+(isActive()?"1":"0"));

		return activity;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject createJSON() {
		JSONObject obj = new JSONObject();
		
		obj.put("id",getId());
		obj.put("name",getName());
		obj.put("active", (isActive()?true:false));
		obj.put("note", getNote());
		obj.put("project", getProject());
		obj.put("favourite", false);
		obj.put("workedTime", (isWorkedTime()?true:false));
		obj.put("rate",0);
		obj.put("rateType","perTask");

		return obj;
	}

}
