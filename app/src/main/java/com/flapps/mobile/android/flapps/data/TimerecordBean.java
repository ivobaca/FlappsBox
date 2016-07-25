package com.flapps.mobile.android.flapps.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Context;
import android.util.Log;

public class TimerecordBean {
	public long id=0;
	public long extId=0;
	public int position=0;
	public int activity=0;
	public int duration=0;
	public boolean workedTime = false;
	public String note = "";
	public String internalNote = "";
	public int activityId = 0;
	public int projectId = 0;
	public boolean isAbsence = false;
	public boolean isBillable = false;
	public Calendar cal;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getExtId() {
		return extId;
	}
	public void setExtId(long l) {
		this.extId = l;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getActivity() {
		return activity;
	}
	public void setActivity(int activity) {
		this.activity = activity;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public boolean isWorkedTime() {
		return workedTime;
	}
	public void setWorkedTime(boolean workedTime) {
		this.workedTime = workedTime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getInternalNote() {
		return internalNote;
	}
	public void setInternalNote(String internalNote) {
		this.internalNote = internalNote;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public boolean isAbsence() {
		return isAbsence;
	}
	public void setAbsence(boolean isAbsence) {
		this.isAbsence = isAbsence;
	}
	public boolean isBillable() {
		return isBillable;
	}
	public void setBillable(boolean isBillable) {
		this.isBillable = isBillable;
	}	
	public Calendar getCal() {
		return cal;
	}
	public void setCal(Calendar cal) {
		this.cal = cal;
	}	
	
	
	public TimerecordBean() {
		super();
		setExtId( System.currentTimeMillis() );
		setCal(Calendar.getInstance());
	}
	//	actions
	public void save( Context cx ) throws TransformerConfigurationException, IOException, ParserConfigurationException, SAXException, TransformerException {
		
		DataModel dm = new DataModel(cx);
		dm.storeTimerecord(this);

		/*
		
		File file = cx.getFileStreamPath(Globals.timerecordfile);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder; 
		Document doc;
		Node rootNode;
		int id = 1;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			if (file.exists()) {
				doc = dBuilder.parse( (InputStream) cx.openFileInput(Globals.timerecordfile) );
				rootNode = doc.getElementsByTagName("flapps").item(0);
				NodeList trs = doc.getElementsByTagName("timerecord");
				for ( int i = 0; i < trs.getLength(); i++ ) {
					Node tr = trs.item(i);
					NamedNodeMap nnm = tr.getAttributes();
					String sid = nnm.getNamedItem("extId").getNodeValue();
					try {
						id = Integer.parseInt(sid) + 1;
					}catch(Exception e) {}
				}
			}
			else {
				doc = dBuilder.newDocument();
				rootNode = doc.createElement("flapps");
				doc.appendChild( rootNode );
				
			}
			Node timeSheet = doc.createElement("dailySheet");
			Node timerecord = doc.createElement("timerecord");

			rootNode.appendChild( timeSheet) ;
			timeSheet.appendChild(timerecord );
			
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd"); 
			((Element) timeSheet).setAttribute("startTime", "00:00:00");
			((Element) timeSheet).setAttribute("date", formater.format(c.getTime()));
			
			((Element) timerecord).setAttribute("id", ""+getId());
			((Element) timerecord).setAttribute("duration", ""+getDuration());
			((Element) timerecord).setAttribute("note", getNote());
			((Element) timerecord).setAttribute("activityId", ""+getActivityId());
			((Element) timerecord).setAttribute("extId", ""+getExtId());
			if (getProjectId()>0)
				((Element) timerecord).setAttribute("projectId", ""+getProjectId());
			else ((Element) timerecord).setAttribute("projectId", "0");
			((Element) timerecord).setAttribute("billable", (isBillable())?"true":"false");
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.transform(new DOMSource(doc),
			      new StreamResult(buffer));
			String str = buffer.toString();
			
			FileOutputStream fos = cx.openFileOutput(Globals.timerecordfile, Context.MODE_PRIVATE);
			fos.write( str.getBytes() );
			fos.close();
			
			FileOutputStream fos2 = cx.openFileOutput("debug", Context.MODE_PRIVATE);
			fos2.write( str.getBytes() );
			fos2.close();
			
			try {
				fos = cx.openFileOutput("params.log", Context.MODE_PRIVATE);
				fos.write( str.getBytes() );
				fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			new DataConnector(cx).execute("saveActivity", str);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public void remove( Context cx, Calendar c ) throws TransformerConfigurationException, IOException, ParserConfigurationException, SAXException, TransformerException {
		
		DataModel dm = new DataModel(cx);
		dm.removeTimerecord(this);
		
		/*
		File file = cx.getFileStreamPath(Globals.timerecordfile);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder; 
		Document doc;
		Node rootNode;
		int id = 1;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			if (file.exists()) {
				doc = dBuilder.parse( (InputStream) cx.openFileInput(Globals.timerecordfile) );
				rootNode = doc.getElementsByTagName("flapps").item(0);
				NodeList trs = doc.getElementsByTagName("timerecord");
				for ( int i = 0; i < trs.getLength(); i++ ) {
					Node tr = trs.item(i);
					NamedNodeMap nnm = tr.getAttributes();
					String sid = nnm.getNamedItem("extId").getNodeValue();
					try {
						id = Integer.parseInt(sid) + 1;
					}catch(Exception e) {}
				}
			}
			else {
				doc = dBuilder.newDocument();
				rootNode = doc.createElement("flapps");
				doc.appendChild( rootNode ); 
				
			}
			Node timeSheet = doc.createElement("dailySheet");
			Node timerecord = doc.createElement("timerecord");

			rootNode.appendChild( timeSheet) ;
			timeSheet.appendChild(timerecord );
			
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd"); 
			((Element) timeSheet).setAttribute("startTime", "00:00:00");
			((Element) timeSheet).setAttribute("date", formater.format(c.getTime()));
			
			((Element) timerecord).setAttribute("id", ""+getId());
			if (getExtId()==0)
				((Element) timerecord).setAttribute("extId", "");
			else ((Element) timerecord).setAttribute("extId", ""+getExtId());
			((Element) timerecord).setAttribute("delete", "true");
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.transform(new DOMSource(doc),
			      new StreamResult(buffer));
			String str = buffer.toString();
			
			FileOutputStream fos = cx.openFileOutput(Globals.timerecordfile, Context.MODE_PRIVATE);
			fos.write( str.getBytes() );
			fos.close();
			
			try {
				fos = cx.openFileOutput("params.log", Context.MODE_PRIVATE);
				fos.write( str.getBytes() );
				fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//new DataConnector(cx).execute("removeActivity", str);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

	}
	
	public void load( Node timerecord) {
		NamedNodeMap attrs = timerecord.getAttributes();
	
		Log.v("TimerecordBean", "run");
		Log.v("TimerecordBean", attrs.getNamedItem("id").getNodeValue()); 
		
		if ( attrs.getNamedItem("id") != null )
			setId(Long.parseLong(attrs.getNamedItem("id").getNodeValue().trim()));
		if ( attrs.getNamedItem("extId") != null )
			setExtId(Long.parseLong(attrs.getNamedItem("extId").getNodeValue().trim()));
		if ( attrs.getNamedItem("position") != null )
			setPosition(Integer.parseInt(attrs.getNamedItem("position").getNodeValue()));
		if ( attrs.getNamedItem("activityId") != null )
			setActivity(Integer.parseInt(attrs.getNamedItem("activityId").getNodeValue()));
		if ( attrs.getNamedItem("activityId") != null )
			setActivityId(Integer.parseInt(attrs.getNamedItem("activityId").getNodeValue()));
		if ( attrs.getNamedItem("duration") != null )
			setDuration(Integer.parseInt(attrs.getNamedItem("duration").getNodeValue()));
		if ( attrs.getNamedItem("workedTime") != null )
			setWorkedTime(("true".equals(attrs.getNamedItem("workedTime").getNodeValue())));
		if ( attrs.getNamedItem("note") != null )
			setNote(attrs.getNamedItem("note").getNodeValue());
		if ( attrs.getNamedItem("internalNote") != null )
			setInternalNote(attrs.getNamedItem("internalNote").getNodeValue());
		if ( attrs.getNamedItem("projectId") != null )
			setProjectId(Integer.parseInt(attrs.getNamedItem("projectId").getNodeValue()));
		if ( attrs.getNamedItem("isAbsence") != null )
			setAbsence("true".equals(attrs.getNamedItem("isAbsence").getNodeValue()));
		if ( attrs.getNamedItem("billable") != null )
			setBillable("true".equals(attrs.getNamedItem("billable").getNodeValue()));
	}
	
	public Element createElement( Document doc , boolean delete ) {
		Element timerecord = doc.createElement("timerecord");

		((Element) timerecord).setAttribute("id", ""+getId());
		((Element) timerecord).setAttribute("duration", ""+getDuration());
		((Element) timerecord).setAttribute("note", getNote());
		((Element) timerecord).setAttribute("activityId", ""+getActivityId());
		((Element) timerecord).setAttribute("extId", ""+getExtId());
//		((Element) timerecord).setAttribute("extId", "999" );
		if (delete) ((Element) timerecord).setAttribute("delete", "true");
		((Element) timerecord).setAttribute("projectId", ""+getProjectId());
		((Element) timerecord).setAttribute("billable", (isBillable())?"true":"false");
		
		return timerecord;
	}
}
