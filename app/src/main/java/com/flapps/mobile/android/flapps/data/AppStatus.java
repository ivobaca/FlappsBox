package com.flapps.mobile.android.flapps.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.jasypt.util.text.BasicTextEncryptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Context;
import android.util.Log;

public class AppStatus {
	
	private Context context;
	private BasicTextEncryptor textEncryptor;
	
	public AppStatus(Context context) {
		super();
		this.context = context;
		
		textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("ED8*Ket56Dww(3rty$$dertr43s@hejAAh");
		
		try {
			initFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean showIntro() {
		
		try {
			Document doc = readFile();
			NodeList nl = doc.getElementsByTagName("intro");
			if ( nl.getLength() == 0 ) return true;
			if ( !"shown".equals( nl.item(0).getChildNodes().item(0).getNodeValue() ) ) return true; 
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		
		return false;
	}
	
	public void setShowIntro(String value) {
		Document doc;
		try {
			doc = readFile();
			NodeList nl = doc.getElementsByTagName("intro");
			if ( nl.getLength() > 0 ) {
				Node intro = nl.item(0);
				Node text = intro.getChildNodes().item(0);
				intro.replaceChild(doc.createTextNode(value), text);
			}
			else { 
				Node status = doc.getElementsByTagName("status").item(0);
				Node intro = doc.createElement("intro");
				intro.appendChild(doc.createTextNode(value));
				status.appendChild(intro);
			}
			saveFile( doc);
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public String getLastSync() {
		
		try {
			Document doc = readFile();
			NodeList nl = doc.getElementsByTagName("sync");
			if ( nl.getLength() == 0 ) return "";
			NodeList nl2 = ((Element) nl.item(0)).getElementsByTagName("last");
			return nl2.item(0).getChildNodes().item(0).getNodeValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void setLastSyncNow () {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy H:mm");
		setLastSync( sdf.format( Calendar.getInstance().getTime() ) );
	}
	
	public void setLastSync( String value ) {
		Document doc;
		try {
			doc = readFile();
			NodeList nl = doc.getElementsByTagName("sync");
			if ( nl.getLength() > 0 ) {
				Node sync = nl.item(0);
				Node last = ((Element) sync).getElementsByTagName("last").item(0);
				Node newlast = doc.createElement("last");
				newlast.appendChild(doc.createTextNode(value));
				sync.replaceChild(newlast, last);
			}
			else { 
				Node status = doc.getElementsByTagName("status").item(0);
				Node sync = doc.createElement("sync");
				Node newlast = doc.createElement("last");
				newlast.appendChild(doc.createTextNode(value));
				Node newinterval = doc.createElement("interval");
				newinterval.appendChild(doc.createTextNode(""));
				sync.appendChild(newlast);
				sync.appendChild(newinterval);
				status.appendChild(sync);
			}
			saveFile( doc);
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public int getSyncInterval() {
		
		try {
			Document doc = readFile();
			NodeList nl = doc.getElementsByTagName("sync");
			if ( nl.getLength() == 0 ) return 0;
			NodeList nl2 = ((Element) nl.item(0)).getElementsByTagName("interval");
			if ( nl2.getLength() == 0 ) return 0;
			if ( nl2.item(0).getChildNodes().getLength() == 0) return 0;
			return Integer.parseInt( nl2.item(0).getChildNodes().item(0).getNodeValue() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void setSyncInterval( int value ) {
		Document doc;
		try {
			doc = readFile();

			Node status2 = doc.getElementsByTagName("status").item(0);
			NodeList nl2 = doc.getElementsByTagName("sync");
			status2.removeChild(nl2.item(0));

			NodeList nl = doc.getElementsByTagName("sync");
			if ( nl.getLength() > 0 ) {
				Node sync = nl.item(0);
				Node interval = ((Element) sync).getElementsByTagName("interval").item(0);
				Node newinterval = doc.createElement("interval");
				newinterval.appendChild(doc.createTextNode(""+value));
				sync.replaceChild(newinterval, interval);
			}
			else { 
				Node status = doc.getElementsByTagName("status").item(0);
				Node sync = doc.createElement("sync");
				Node newlast = doc.createElement("last");
				newlast.appendChild(doc.createTextNode(""));
				Node newinterval = doc.createElement("interval");
				newinterval.appendChild(doc.createTextNode(""+value));
				sync.appendChild(newlast);
				sync.appendChild(newinterval);
				status.appendChild(sync);
			}
			saveFile( doc);
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public String[] getCredentials() {
		String[] c = new String[3];
		
		try {
			Document doc = readFile();
			Node creds = doc.getElementsByTagName("credentials").item(0);
			if (creds!=null) {
				c[0] = ((Element) creds).getElementsByTagName("domain").item(0).getChildNodes().item(0).getNodeValue();
				c[1] = ((Element) creds).getElementsByTagName("login").item(0).getChildNodes().item(0).getNodeValue();
				c[2] = textEncryptor.decrypt( ((Element) creds).getElementsByTagName("password").item(0).getChildNodes().item(0).getNodeValue() );
				Globals.server =  ((Element) creds).getElementsByTagName("server").item(0).getChildNodes().item(0).getNodeValue();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return c;
	}
	
	public void setCredentials( String domain, String login, String password ) {

		try {
			Document doc = readFile();
			Node cred = doc.createElement("credentials");
			Element e = doc.createElement("domain");
			Text t = doc.createTextNode(domain);
			e.appendChild(t);
			cred.appendChild(e);
			e = doc.createElement("login");
			t = doc.createTextNode(login);
			e.appendChild(t);
			cred.appendChild(e);
			e = doc.createElement("server");
			t = doc.createTextNode(Globals.server);
			e.appendChild(t);
			cred.appendChild(e);
			e = doc.createElement("password");
			t = doc.createTextNode( textEncryptor.encrypt(password));
			e.appendChild(t);
			cred.appendChild(e);
			
			Node status = doc.getElementsByTagName("status").item(0);

			NodeList nl = doc.getElementsByTagName("credentials");
			if ( nl.getLength() == 0 ) {
				status.appendChild(cred);
			} else {
				status.replaceChild(cred, nl.item(0));
			}
			
			saveFile(doc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Document readFile() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse( (InputStream) context.openFileInput(Globals.statusfile) );
	 
		return doc;
	}
	
	private void initFile() throws ParserConfigurationException, TransformerException, SAXException, IOException {
		File file = context.getFileStreamPath(Globals.statusfile);
		if(file.exists()) {
			Document doc = readFile();
			NodeList nl1 = doc.getElementsByTagName("status");
			NodeList nl2 = doc.getElementsByTagName("intro");
			if (nl1.getLength()==1 && nl2.getLength()==1) 
				return;
		}
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();
		
		Element rootElement = doc.createElement("status");
		doc.appendChild(rootElement);
		
		Element introElement = doc.createElement("intro");
		introElement.appendChild(doc.createTextNode("none"));
		
		rootElement.appendChild(introElement);
		saveFile(doc); 
	}
	
	private void saveFile(Document doc) throws FileNotFoundException, TransformerException {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(context.openFileOutput(
				Globals.statusfile, Context.MODE_PRIVATE)); 
		transformer.transform(source, result);
	}

}
