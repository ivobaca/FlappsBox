package com.flapps.mobile.android.flapps.data;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.content.Context;

public class ProjectBean implements Comparable<ProjectBean> {
	
	public int id=0;
	//public int extId=0;
	public String name="";
	public int clientId=0;
	//public int clientExtId=0;
	public ClientBean client;
	
	
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
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	/*
	public int getExtId() {
		return extId;
	}
	public void setExtId(int extId) {
		this.extId = extId;
	}
	*/
	public ClientBean getClient() {
		return client;
	}
	public void setClient(ClientBean client) {
		this.client = client;
	}	
	/*
	public int getClientExtId() {
		return clientExtId;
	}
	public void setClientExtId(int clientExtId) {
		this.clientExtId = clientExtId;
	}
	*/
	
	//	actions
	public void load( Node activity ) {
		NamedNodeMap attrs = activity.getAttributes();
		
		if ( attrs.getNamedItem("id") != null ) setId(Integer.parseInt(attrs.getNamedItem("id").getNodeValue()));
		if ( attrs.getNamedItem("name") != null ) setName(attrs.getNamedItem("name").getNodeValue());
		if ( attrs.getNamedItem("clientId") != null ) setClientId(Integer.parseInt(attrs.getNamedItem("clientId").getNodeValue()));
		//if ( attrs.getNamedItem("extId") != null ) setExtId( Integer.parseInt(attrs.getNamedItem("extId").getNodeValue()) );
	}
	@Override
	public int compareTo(ProjectBean another) {

		return another.getName().compareTo(this.getName());
	}

	//	actions
	public void save( Context cx ) throws TransformerConfigurationException, IOException, ParserConfigurationException, SAXException, TransformerException {
		
		DataModel dm = new DataModel(cx);
		dm.storeProject(this);
	}

	public Element createElement( Document doc ) {
		Element project = doc.createElement("project");
		
		int id = getId();
		//if ( id == 0 ) id = -1 * getExtId();
		((Element) project).setAttribute("id", ""+id);
		//((Element) project).setAttribute("extId", ""+getExtId());
		((Element) project).setAttribute("name", ""+getName());
		((Element) project).setAttribute("clientId", ""+getClientId());

		return project;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject createJSON() {
		JSONObject obj = new JSONObject();
		
		obj.put("name",getName());
		obj.put("description", "");
		obj.put("duration", 0);
		obj.put("billable", "false");
		obj.put("clientId", getClientId());
		//obj.put("clientExtId", getClientExtId());
		obj.put("rate", 0);
		obj.put("active", "true");
		obj.put("budget", 0);
		obj.put("publicProject", "true");
		obj.put("code", "");
		obj.put("favourite", "false");
		obj.put("id", getId());
		
		return obj;
	}
}
