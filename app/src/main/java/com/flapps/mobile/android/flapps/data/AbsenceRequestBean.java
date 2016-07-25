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

public class AbsenceRequestBean {
	
	int duration=0;
	Calendar startDate=null;
	Calendar endDate=null;
	String name="";
	String status="";
	String note="";
	int startLength=1;
	int endLength=1;
	int absenceTypeId = 0;
	long extId = 0;
	
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Calendar getStartDate() {
		return startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	public Calendar getEndDate() {
		return endDate;
	}
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getStartLength() {
		return startLength;
	}
	public void setStartLength(int startLength) {
		this.startLength = startLength;
	}
	public int getEndLength() {
		return endLength;
	}
	public void setEndLength(int endLength) {
		this.endLength = endLength;
	}
	public int getAbsenceTypeId() {
		return absenceTypeId;
	}
	public void setAbsenceTypeId(int absenceTypeId) {
		this.absenceTypeId = absenceTypeId;
	}
	public long getExtId() {
		return extId;
	}
	public void setExtId(long extId) {
		this.extId = extId;
	}
	public void load( Node absence) {
		NamedNodeMap attrs = absence.getAttributes();
	
		if ( attrs.getNamedItem("duration") != null )
			setDuration(Integer.parseInt(attrs.getNamedItem("duration").getNodeValue().trim()));
		if ( attrs.getNamedItem("startDate") != null ) {
			startDate = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				startDate.setTime(sdf.parse( attrs.getNamedItem("startDate").getNodeValue() ));
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ( attrs.getNamedItem("endDate") != null ) {
			endDate = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				endDate.setTime(sdf.parse( attrs.getNamedItem("endDate").getNodeValue() ));
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ( attrs.getNamedItem("name") != null )
			setName(attrs.getNamedItem("name").getNodeValue());
		if ( attrs.getNamedItem("status") != null )
			setStatus(attrs.getNamedItem("status").getNodeValue());
		if ( attrs.getNamedItem("note") != null )
			setNote(attrs.getNamedItem("note").getNodeValue());
		if ( attrs.getNamedItem("startLength") != null )
			setStartLength(Integer.parseInt(attrs.getNamedItem("startLength").getNodeValue()));
		if ( attrs.getNamedItem("endLength") != null )
			setEndLength(Integer.parseInt(attrs.getNamedItem("endLength").getNodeValue()));
		if ( attrs.getNamedItem("extId") != null )
			setExtId(Long.parseLong(attrs.getNamedItem("extId").getNodeValue()));
		else setExtId(0);
	}



	//	actions
	public boolean check() {
		
		if (getStartDate()== null) return false;
		if (getEndDate()== null) return false;
		if (getAbsenceTypeId() <= 0) return false;

		return true;
	}
	
	public Element createElement( Document doc, boolean full ) {
		Element absenceRequest = doc.createElement("absenceRequest");
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd"); 

		((Element) absenceRequest).setAttribute("startDate", formater.format(getStartDate().getTime()));
		((Element) absenceRequest).setAttribute("endDate", formater.format(getEndDate().getTime()));
		
		((Element) absenceRequest).setAttribute("startLength", ""+(getStartLength() *240 ));
		((Element) absenceRequest).setAttribute("endLength", ""+(getEndLength() * 240));
		((Element) absenceRequest).setAttribute("note", ""+getNote());
		if (full) ((Element) absenceRequest).setAttribute("name", ""+getName());
		((Element) absenceRequest).setAttribute("extId", ""+getExtId());
		((Element) absenceRequest).setAttribute("absenceTypeId", ""+getAbsenceTypeId());

		return absenceRequest;
	}

	public void save( Context cx ) throws TransformerConfigurationException, IOException, ParserConfigurationException, SAXException, TransformerException {
		
		
		DataModel dm = new DataModel(cx);
		dm.storeAbsenceRequest(this);

		/*
		File file = cx.getFileStreamPath(Globals.absencesfile);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder; 
		Document doc;
		Node rootNode;
		int id = 1;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			if (file.exists()) {
				doc = dBuilder.parse( (InputStream) cx.openFileInput(Globals.absencesfile) );
				rootNode = doc.getElementsByTagName("flapps").item(0);
				NodeList trs = doc.getElementsByTagName("absenceRequest");
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
			Node absenceRequest = doc.createElement("absenceRequest");

			rootNode.appendChild( absenceRequest) ;
			
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd"); 
			((Element) absenceRequest).setAttribute("startDate", formater.format(getStartDate().getTime()));
			((Element) absenceRequest).setAttribute("endDate", formater.format(getEndDate().getTime()));
			
			((Element) absenceRequest).setAttribute("startLength", ""+(getStartLength() *240 ));
			((Element) absenceRequest).setAttribute("endLength", ""+(getEndLength() * 240));
			((Element) absenceRequest).setAttribute("note", ""+getNote());
			((Element) absenceRequest).setAttribute("extId", ""+id);
			((Element) absenceRequest).setAttribute("absenceTypeId", ""+getAbsenceTypeId());
			
			//	<absenceRequest startDate=\"%@\" endDate=\"%@\" startLength=\"%d\" endLength=\"%d\" note=\"%@\" absenceTypeId=\"%d\" extId=\"%@\"/>

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.transform(new DOMSource(doc),
			      new StreamResult(buffer));
			String str = buffer.toString();
			
			FileOutputStream fos = cx.openFileOutput(Globals.absencesfile, Context.MODE_PRIVATE);
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

			new DataConnector(cx).execute("saveAbsence", str);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	public int compareTo(AbsenceRequestBean another) {
		return another.getStartDate().compareTo(this.getStartDate());
	}
}
