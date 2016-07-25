package com.flapps.mobile.android.flapps.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Context;
import android.util.Log;

public class ExpenseBean {

		long id = 0;
		Calendar date = null;
		String type = "";
		int typeId = 0;
		String client = "";
		int clientId = 0;
		String project = "";
		int projectId = 0;
		double amount = 0;
		String state = "";
		String note = "";
		long extId = 0;
		
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public int getTypeId() {
			return typeId;
		}
		public void setTypeId(int typeId) {
			this.typeId = typeId;
		}
		public Calendar getDate() {
			return date;
		}
		public void setDate(Calendar date) {
			this.date = date;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getClient() {
			return client;
		}
		public void setClient(String client) {
			this.client = client;
		}
		public int getClientId() {
			return clientId;
		}
		public void setClientId(int clientId) {
			this.clientId = clientId;
		}
		public String getProject() {
			return project;
		}
		public void setProject(String project) {
			this.project = project;
		}
		public int getProjectId() {
			return projectId;
		}
		public void setProjectId(int projectId) {
			this.projectId = projectId;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public long getExtId() {
			return extId;
		}
		public void setExtId(long extId) {
			this.extId = extId;
		}
		
		
		public void load( Node expence) {
			NamedNodeMap attrs = expence.getAttributes();
		
			if ( attrs.getNamedItem("id") != null )
				setId(Integer.parseInt(attrs.getNamedItem("id").getNodeValue().trim()));
			else setId(0);
			if ( attrs.getNamedItem("date") != null ) {
				date = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date.setTime(sdf.parse( attrs.getNamedItem("date").getNodeValue() ));
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if ( attrs.getNamedItem("type") != null )
				setType(attrs.getNamedItem("type").getNodeValue());
			if ( attrs.getNamedItem("client") != null )
				setClient(attrs.getNamedItem("client").getNodeValue());
			if ( attrs.getNamedItem("clientId") != null )
				setClientId(Integer.parseInt(attrs.getNamedItem("clientId").getNodeValue()));
			if ( attrs.getNamedItem("project") != null )
				setProject(attrs.getNamedItem("project").getNodeValue());
			if ( attrs.getNamedItem("projectId") != null )
				setProjectId(Integer.parseInt(attrs.getNamedItem("projectId").getNodeValue()));
			if ( attrs.getNamedItem("amount") != null )
				setAmount(Double.parseDouble(attrs.getNamedItem("amount").getNodeValue()));
			if ( attrs.getNamedItem("state") != null )
				setState(attrs.getNamedItem("state").getNodeValue());
			if ( attrs.getNamedItem("note") != null )
				setNote(attrs.getNamedItem("note").getNodeValue());
			if ( attrs.getNamedItem("extId") != null )
				setExtId(Integer.parseInt(attrs.getNamedItem("extId").getNodeValue()));
			else setExtId(0);
		}
		
		public Element createElement( Document doc, boolean full ) {
			Element expense = doc.createElement("expense");
			
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd"); 
			((Element) expense).setAttribute("date", formater.format(getDate().getTime()));
			
			((Element) expense).setAttribute("id", ""+getId());
			((Element) expense).setAttribute("amount", ""+getAmount());
			((Element) expense).setAttribute("clientId", ""+getClientId());
			if (full) ((Element) expense).setAttribute("client", ""+getClient());
			((Element) expense).setAttribute("note", ""+getNote());
			((Element) expense).setAttribute("extId", ""+getExtId());
			((Element) expense).setAttribute("projectId", ""+getProjectId());
			if (full) ((Element) expense).setAttribute("project", ""+getProject());
			((Element) expense).setAttribute("expTypeId", ""+getTypeId());
			if (full) ((Element) expense).setAttribute("type", ""+getType());

			return expense;
		}

		//	actions
		public boolean check() {
			
			if (getDate()== null) return false;
			if (getAmount() <= 0) return false;
			if (getClientId() <= 0) return false;
			if (getProjectId() <= 0) return false;
			if (getTypeId() <= 0) return false;
			return true;
		}
		
		public void save( Context cx ) throws TransformerConfigurationException, IOException, ParserConfigurationException, SAXException, TransformerException {
			
			DataModel dm = new DataModel(cx);
			dm.storeExpense(this);
		}
}
