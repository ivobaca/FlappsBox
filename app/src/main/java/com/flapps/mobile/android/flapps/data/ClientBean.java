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

public class ClientBean implements Comparable<ClientBean> {

	 public int id=0;
	 //public int extId=0;
	 public String contactName="";
	 public String email="";
	 public String name="";
	 
	 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName (String contactName) {
		this.contactName = contactName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/*
	public int getExtId() {
		return extId;
	}
	public void setExtId(int extId) {
		this.extId = extId;
	}
	*/

	//	actions
	public void save( Context cx ) throws TransformerConfigurationException, IOException, ParserConfigurationException, SAXException, TransformerException {
		
		DataModel dm = new DataModel(cx);
		dm.storeClient(this);
	}


	public void load( Node activity ) {
		NamedNodeMap attrs = activity.getAttributes();
		
		if ( attrs.getNamedItem("id") != null ) setId(Integer.parseInt(attrs.getNamedItem("id").getNodeValue()));
		if ( attrs.getNamedItem("name") != null ) setName(attrs.getNamedItem("name").getNodeValue());
		if ( attrs.getNamedItem("contactName") != null ) setContactName(attrs.getNamedItem("contactName").getNodeValue());
		if ( attrs.getNamedItem("email") != null ) setEmail( attrs.getNamedItem("email").getNodeValue() );
		//if ( attrs.getNamedItem("extId") != null ) setExtId( Integer.parseInt(attrs.getNamedItem("extId").getNodeValue()) );
	}
	@Override
	public int compareTo(ClientBean another) {
		return this.getName().compareTo(another.getName());
	}
	
	public Element createElement( Document doc ) {
		Element client = doc.createElement("client");
		
		int id = getId();
		//if ( id == 0 ) id = getExtId();
		((Element) client).setAttribute("id", ""+id);
		//((Element) client).setAttribute("extId", ""+getExtId());
		((Element) client).setAttribute("name", ""+getName());
		
		((Element) client).setAttribute("contactName", ""+getContactName());
		((Element) client).setAttribute("email", ""+getEmail());

		return client;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject createJSON() {
		JSONObject obj = new JSONObject();
		
		obj.put("client",getName());
		obj.put("description", "");
		obj.put("name", "");
		obj.put("phone", "");
		obj.put("position", "");
		obj.put("active", "true");
		obj.put("email", "");
		obj.put("id", getId());
		//if ( getExtId() > 0  ) obj.put("extId", getExtId());
		
		return obj;
	}

}
