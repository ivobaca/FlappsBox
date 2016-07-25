package com.flapps.mobile.android.flapps.data;

import com.flapps.mobile.android.flapps.ComeInOutActivity;
import com.flapps.mobile.android.flapps.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.commons.io.IOUtils;

import com.flapps.mobile.android.flapps.CostsNewActivity;
import com.flapps.mobile.android.flapps.MainActivity;
import com.flapps.mobile.android.flapps.data.attendance.Work;
import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class DataModel {

	Context context;
	Document data;

	public static boolean exists(Context cx) {
		File file = cx.getFileStreamPath(Globals.datafile);
		return file.exists();
	}

	public DataModel(Context cx) {
		context = cx;

		try {
			if (DataModel.exists(cx))
				data = readFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Document readFile() throws ParserConfigurationException,
			FileNotFoundException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse((InputStream) context
				.openFileInput(Globals.datafile));

		return doc;
	}

	public static void saveFile(Context context, String returned)
			throws IOException {

		if (returned.indexOf("<html>") > -1) {
			Log.v("DataModel", "bad file");
			return;
		}
		/*
		 * DocumentBuilderFactory dbFactory =
		 * DocumentBuilderFactory.newInstance(); DocumentBuilder dBuilder; try {
		 * dBuilder = dbFactory.newDocumentBuilder(); Document doc =
		 * dBuilder.parse(returned); //doc.getElementsByTagName("flapps"); }
		 * catch (ParserConfigurationException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); return; } catch (SAXException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); return; }
		 */

		FileOutputStream fos = context.openFileOutput(Globals.datafile,
				Context.MODE_PRIVATE);
		fos.write(returned.getBytes());
		fos.close();
		AppStatus as = new AppStatus(context);
		as.setLastSyncNow();
	}
	
    protected Object readObject(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream read= new ObjectInputStream (new FileInputStream( context.getFilesDir().toString()+ "/" + filename));
        Object obj = read.readObject();
        read.close();

        return obj;
    }

    protected void saveObject( Object obj, String filename ) throws IOException {

        ObjectOutputStream write= new ObjectOutputStream (new FileOutputStream( context.getFilesDir().toString()+ "/" + filename));
        write.writeObject(obj);
        write.close();
    }

	public Document getDoc() {
		return data;
	}

	public ActivityHashMap getActivities() {
		return new ActivityHashMap(data);
	}

	public ClientHashMap getClients() {
		return new ClientHashMap(data);
	}

	public ProjectHashMap getProjects() {
		ProjectHashMap phm = new ProjectHashMap(data);
		return phm;
	}

	public ProjectHashMap getProjects(int id) {
		ProjectHashMap phm = new ProjectHashMap(data, id);
		return phm;
	}

	public TimerecordHashMap getTimerecords() {
		return new TimerecordHashMap(data);
	}

	public TimerecordHashMap getTimerecords(Calendar c) {
		return new TimerecordHashMap(data, c);
	}

	public ExpenseHashMap getExpences() {
		return new ExpenseHashMap(data);
	}

	public void storeExpense(ExpenseBean eb) throws IOException,
			ParserConfigurationException, SAXException,
			TransformerConfigurationException, TransformerException {

		Element updateelement = null;
		Element el = null;

		NodeList els = data.getElementsByTagName("expenseList");
		if (els.getLength() == 0) {
			NodeList nls = data.getElementsByTagName("flapps");
			Node nl = nls.item(0);
			el = data.createElement("expenseList");
			nl.appendChild(el);
			updateelement = null;
		} else {
			el = (Element) els.item(0);
			NodeList eelements = el.getChildNodes();
			for (int i = 0; i < eelements.getLength(); i++) {
				Element eelement = (Element) eelements.item(i);
				if (eb.getId() == 0) {
					if (eelement.getAttribute("extId").equals(
							"" + eb.getExtId()))
						updateelement = eelement;
				} else {
					if (eelement.getAttribute("id").equals("" + eb.getExtId()))
						updateelement = eelement;
				}
			}
		}
		el = (Element) els.item(0);
		if (updateelement != null)
			el.replaceChild(eb.createElement(data, true), updateelement);
		else
			el.appendChild(eb.createElement(data, true));

		saveXml(Globals.datafile, data);

		updateelement = null;
		el = null;
		File file = context.getFileStreamPath(Globals.expensesfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		Node rootNode;

		dBuilder = dbFactory.newDocumentBuilder();
		if (file.exists()) {
			doc = dBuilder.parse((InputStream) context
					.openFileInput(Globals.expensesfile));
			rootNode = doc.getElementsByTagName("flapps").item(0);
			NodeList eelements = rootNode.getChildNodes();
			for (int i = 0; i < eelements.getLength(); i++) {
				Element eelement = (Element) eelements.item(i);
				if (eb.getId() == 0) {
					if (eelement.getAttribute("extId").equals(
							"" + eb.getExtId()))
						updateelement = eelement;
				} else {
					if (eelement.getAttribute("id").equals("" + eb.getExtId()))
						updateelement = eelement;
				}
			}
		} else {
			doc = dBuilder.newDocument();
			rootNode = doc.createElement("flapps");
			doc.appendChild(rootNode);
		}
		if (updateelement != null)
			rootNode.replaceChild(eb.createElement(doc, false), updateelement);
		else
			rootNode.appendChild(eb.createElement(doc, false));

		saveXml(Globals.expensesfile, doc);
	}

	public void storeAbsenceRequest(AbsenceRequestBean eb) throws IOException,
			ParserConfigurationException, SAXException,
			TransformerConfigurationException, TransformerException {

		Element updateelement = null;
		Element el = null;

		NodeList els = data.getElementsByTagName("absenceRequestList");
		if (els.getLength() == 0) {
			NodeList nls = data.getElementsByTagName("flapps");
			Node nl = nls.item(0);
			el = data.createElement("absenceRequestList");
			nl.appendChild(el);
			updateelement = null;
		} else {
			el = (Element) els.item(0);
			NodeList eelements = el.getChildNodes();
			for (int i = 0; i < eelements.getLength(); i++) {
				Element eelement = (Element) eelements.item(i);
				if (eelement.getAttribute("extId").equals("" + eb.getExtId()))
					updateelement = eelement;
			}
		}
		el = (Element) els.item(0);
		if (updateelement != null)
			el.replaceChild(eb.createElement(data, true), updateelement);
		else
			el.appendChild(eb.createElement(data, true));

		saveXml(Globals.datafile, data);

		updateelement = null;
		el = null;
		File file = context.getFileStreamPath(Globals.absencesfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		Node rootNode;

		dBuilder = dbFactory.newDocumentBuilder();
		if (file.exists()) {
			doc = dBuilder.parse((InputStream) context
					.openFileInput(Globals.absencesfile));
			rootNode = doc.getElementsByTagName("flapps").item(0);
			NodeList eelements = rootNode.getChildNodes();
			for (int i = 0; i < eelements.getLength(); i++) {
				Element eelement = (Element) eelements.item(i);
				if (eelement.getAttribute("extId").equals("" + eb.getExtId()))
					updateelement = eelement;
			}
		} else {
			doc = dBuilder.newDocument();
			rootNode = doc.createElement("flapps");
			doc.appendChild(rootNode);
		}
		if (updateelement != null)
			rootNode.replaceChild(eb.createElement(doc, false), updateelement);
		else
			rootNode.appendChild(eb.createElement(doc, false));

		saveXml(Globals.absencesfile, doc);
	}

	public void storeActivity(ActivityBean ab) throws IOException,
			ParserConfigurationException, SAXException,
			TransformerConfigurationException, TransformerException {

		int updatejson = -1;
		Element updateelement = null;
		Element el = null;

		NodeList nls = data.getElementsByTagName("flapps");
		NodeList els = data.getElementsByTagName("activity");
		for (int i = 0; i < els.getLength(); i++) {
			Element eelement = (Element) els.item(i);
			if (eelement.getAttribute("id").equals("" + ab.getId()))
				updateelement = eelement;
		}
		el = (Element) nls.item(0);
		if (updateelement != null)
			el.replaceChild(ab.createElement(data), updateelement);
		else
			el.appendChild(ab.createElement(data));

		saveXml(Globals.datafile, data);

		updateelement = null;
		el = null;
		File file = context.getFileStreamPath(Globals.activitiesfile);
		JSONArray array = null;
		if (file.exists()) {
			Object obj = JSONValue.parse(IOUtils.toString((InputStream) context
					.openFileInput(Globals.activitiesfile)));
			array = (JSONArray) obj;
			for (int i = 0; i < array.size(); i++) {
				JSONObject eelement = (JSONObject) array.get(i);
				if (eelement.get("id") != null) {
					if (eelement.get("id").equals("" + ab.getId()))
						updatejson = i;
				}
			}
		} else {
			array = new JSONArray();
		}
		if (updatejson >= 0)
			array.remove(updatejson);
		array.add(ab.createJSON());

		saveJSON(Globals.activitiesfile, array);
	}

	public void replaceActivityId(long old, String id)
			throws ParserConfigurationException, FileNotFoundException,
			SAXException, IOException, TransformerConfigurationException,
			TransformerException {
		Log.v("Replace", old + " -> " + id);
		Element updateelement = null;
		Element el = null;
		File file = context.getFileStreamPath(Globals.timerecordfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		Node rootNode;

		dBuilder = dbFactory.newDocumentBuilder();
		if (file.exists()) {
			doc = dBuilder.parse((InputStream) context
					.openFileInput(Globals.timerecordfile));
			rootNode = doc.getElementsByTagName("flapps").item(0);

			NodeList els = doc.getElementsByTagName("dailySheet");
			for (int j = 0; j < els.getLength(); j++) {
				Log.v("Replace", "dailySheet: " + j);
				try {
					el = (Element) els.item(j);
				} catch (Exception e) {
					continue;
				}
				NodeList eelements = el.getChildNodes();
				for (int i = 0; i < eelements.getLength(); i++) {
					Element eelement;
					Log.v("Replace", "timerecord: " + i);
					try {
						eelement = (Element) eelements.item(i);
					} catch (Exception e) {
						continue;
					}

					Log.v("Replace",
							"timerecord: "
									+ eelement.getAttribute("activityId"));

					if (eelement.getAttribute("activityId").equals("" + old)) {

						Log.v("Replace",
								"Found: " + eelement.getAttribute("activityId"));

						updateelement = (Element) eelement.cloneNode(false);
						updateelement.setAttribute("activityId", id);
						el.replaceChild(updateelement, eelement);
					}
				}
			}
			saveXml(Globals.timerecordfile, doc);
			if (Globals.timerecord.activityId == old)
				Globals.timerecord.activityId = Integer.parseInt(id);
		}
	}

	public void storeClient(ClientBean cb) throws IOException,
			ParserConfigurationException, SAXException,
			TransformerConfigurationException, TransformerException {

		int updatejson = -1;
		Element updateelement = null;
		Element el = null;

		NodeList nls = data.getElementsByTagName("flapps");
		NodeList els = data.getElementsByTagName("client");
		for (int i = 0; i < els.getLength(); i++) {
			Element eelement = (Element) els.item(i);
			if (eelement.getAttribute("id").equals("" + cb.getId())) {
				updateelement = eelement;
			}
		}
		el = (Element) nls.item(0);
		if (updateelement != null)
			el.replaceChild(cb.createElement(data), updateelement);
		else
			el.appendChild(cb.createElement(data));

		saveXml(Globals.datafile, data);

		updateelement = null;
		el = null;
		File file = context.getFileStreamPath(Globals.clientsfile);
		JSONArray array = null;
		if (file.exists()) {
			Object obj = JSONValue.parse(IOUtils.toString((InputStream) context
					.openFileInput(Globals.clientsfile)));
			array = (JSONArray) obj;
			for (int i = 0; i < array.size(); i++) {
				JSONObject eelement = (JSONObject) array.get(i);
				if (eelement.get("id") != null) {
					if (eelement.get("id").equals("" + cb.getId()))
						updatejson = i;
				}
			}
		} else {
			array = new JSONArray();
		}
		if (updatejson >= 0)
			array.remove(updatejson);
		array.add(cb.createJSON());

		saveJSON(Globals.clientsfile, array);
	}

	@SuppressWarnings("unchecked")
	public void replaceClientId(long old, String id)
			throws FileNotFoundException, IOException,
			TransformerConfigurationException, TransformerException {
		File file = context.getFileStreamPath(Globals.projectsfile);
		JSONArray array = null;
		if (file.exists()) {
			Object obj = JSONValue.parse(IOUtils.toString((InputStream) context
					.openFileInput(Globals.projectsfile)));
			array = (JSONArray) obj;
			for (int i = 0; i < array.size(); i++) {
				JSONObject eelement = (JSONObject) array.get(i);
				if ((Long) eelement.get("clientId") == old) {
					eelement.put("clientId", id);
				}
			}
			saveJSON(Globals.projectsfile, array);
		}
	}

	public void storeProject(ProjectBean pb) throws IOException,
			ParserConfigurationException, SAXException,
			TransformerConfigurationException, TransformerException {

		int updatejson = -1;
		Element updateelement = null;
		Element el = null;

		NodeList nls = data.getElementsByTagName("flapps");
		NodeList els = data.getElementsByTagName("project");
		for (int i = 0; i < els.getLength(); i++) {
			Element eelement = (Element) els.item(i);
			if (eelement.getAttribute("id").equals("" + pb.getId())) {
				updateelement = eelement;
			}
		}
		el = (Element) nls.item(0);
		if (updateelement != null)
			el.replaceChild(pb.createElement(data), updateelement);
		else
			el.appendChild(pb.createElement(data));

		saveXml(Globals.datafile, data);

		updateelement = null;
		el = null;
		File file = context.getFileStreamPath(Globals.projectsfile);
		JSONArray array = null;
		if (file.exists()) {
			Object obj = JSONValue.parse(IOUtils.toString((InputStream) context
					.openFileInput(Globals.projectsfile)));
			array = (JSONArray) obj;
			for (int i = 0; i < array.size(); i++) {
				JSONObject eelement = (JSONObject) array.get(i);
				if (eelement.get("id") != null) {
					if (eelement.get("id").equals("" + pb.getId()))
						updatejson = i;
				}
			}
		} else {
			array = new JSONArray();
		}
		if (updatejson >= 0)
			array.remove(updatejson);
		array.add(pb.createJSON());

		saveJSON(Globals.projectsfile, array);
	}

	public void replaceProjectId(long old, String id)
			throws FileNotFoundException, SAXException, IOException,
			ParserConfigurationException, TransformerConfigurationException,
			TransformerException {
		Log.v("Replace", old + " -> " + id);
		Element updateelement = null;
		Element el = null;
		File file = context.getFileStreamPath(Globals.timerecordfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		Node rootNode;

		dBuilder = dbFactory.newDocumentBuilder();
		if (file.exists()) {
			doc = dBuilder.parse((InputStream) context
					.openFileInput(Globals.timerecordfile));
			rootNode = doc.getElementsByTagName("flapps").item(0);

			NodeList els = doc.getElementsByTagName("dailySheet");
			for (int j = 0; j < els.getLength(); j++) {
				Log.v("Replace", "dailySheet: " + j);
				try {
					el = (Element) els.item(j);
				} catch (Exception e) {
					continue;
				}
				NodeList eelements = el.getChildNodes();
				for (int i = 0; i < eelements.getLength(); i++) {
					Element eelement;
					Log.v("Replace", "timerecord: " + i);
					try {
						eelement = (Element) eelements.item(i);
					} catch (Exception e) {
						continue;
					}

					Log.v("Replace",
							"timerecord: " + eelement.getAttribute("projectId"));

					if (eelement.getAttribute("projectId").equals("" + old)) {

						Log.v("Replace",
								"Found: " + eelement.getAttribute("projectId"));

						updateelement = (Element) eelement.cloneNode(false);
						updateelement.setAttribute("projectId", id);
						el.replaceChild(updateelement, eelement);
					}
				}
			}
			saveXml(Globals.timerecordfile, doc);
			if (Globals.timerecord.projectId == old)
				Globals.timerecord.projectId = Integer.parseInt(id);
		}
	}

	public void storeTimerecord(TimerecordBean eb) throws IOException,
			ParserConfigurationException, SAXException,
			TransformerConfigurationException, TransformerException {

		Element updateelement = null;
		Element parentelement = null;
		Element el = null;

		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		NodeList els = data.getElementsByTagName("dailySheet");
		if (els.getLength() == 0) {
			NodeList nls = data.getElementsByTagName("flapps");
			Node nl = nls.item(0);
			el = data.createElement("dailySheet");
			((Element) el).setAttribute("startTime", "00:00:00");
			((Element) el).setAttribute("date",
					formater.format(eb.getCal().getTime()));
			nl.appendChild(el);
			updateelement = null;
			parentelement = el;
		} else {
			for (int j = 0; j < els.getLength(); j++) {
				el = (Element) els.item(j);
				NodeList eelements = el.getChildNodes();
				for (int i = 0; i < eelements.getLength(); i++) {
					Element eelement;
					try {
						eelement = (Element) eelements.item(i);
					} catch (Exception e) {
						continue;
					}
					if (eb.getId() == 0) {
						if (eelement.getAttribute("extId").equals(
								"" + eb.getExtId()))
							updateelement = eelement;
					} else {
						if (eelement.getAttribute("id").equals(
								"" + eb.getId()))
							updateelement = eelement;

					}
				}
				if (el.getAttribute("date").equals(
						formater.format(eb.getCal().getTime())))
					parentelement = (Element) els.item(j);
			}
			if (parentelement == null) {
				NodeList nls = data.getElementsByTagName("flapps");
				Node nl = nls.item(0);
				el = data.createElement("dailySheet");
				((Element) el).setAttribute("startTime", "00:00:00");
				((Element) el).setAttribute("date",
						formater.format(eb.getCal().getTime()));
				nl.appendChild(el);
				updateelement = null;
				parentelement = el;
			}
		}

		if (updateelement != null)
			parentelement.replaceChild(eb.createElement(data, true),
					updateelement);
		else
			parentelement.appendChild(eb.createElement(data, true));

		saveXml(Globals.datafile, data);

		updateelement = null;
		el = null;
		File file = context.getFileStreamPath(Globals.timerecordfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		Node rootNode;

		dBuilder = dbFactory.newDocumentBuilder();
		if (file.exists()) {
			doc = dBuilder.parse((InputStream) context
					.openFileInput(Globals.timerecordfile));
			rootNode = doc.getElementsByTagName("flapps").item(0);

			els = doc.getElementsByTagName("dailySheet");
			for (int j = 0; j < els.getLength(); j++) {
				try {
					el = (Element) els.item(j);
				} catch (Exception e) {
					continue;
				}
				NodeList eelements = el.getChildNodes();
				for (int i = 0; i < eelements.getLength(); i++) {
					Element eelement;
					try {
						eelement = (Element) eelements.item(i);
					} catch (Exception e) {
						continue;
					}
					if (eb.getId() == 0) {
						if (eelement.getAttribute("extId").equals(
								"" + eb.getExtId()))
							updateelement = eelement;
					} else {
						if (eelement.getAttribute("id").equals(
								"" + eb.getExtId()))
							updateelement = eelement;

					}
				}
				if (el.getAttribute("date").equals(
						formater.format(eb.getCal().getTime())))
					parentelement = (Element) els.item(j);
			}
			if (parentelement == null) {
				NodeList nls = doc.getElementsByTagName("flapps");
				Node nl = nls.item(0);
				el = doc.createElement("dailySheet");
				((Element) el).setAttribute("startTime", "00:00:00");
				((Element) el).setAttribute("date",
						formater.format(eb.getCal().getTime()));
				nl.appendChild(el);
				updateelement = null;
				parentelement = el;
			}
		} else {
			doc = dBuilder.newDocument();
			rootNode = doc.createElement("flapps");
			doc.appendChild(rootNode);
			el = doc.createElement("dailySheet");
			((Element) el).setAttribute("startTime", "00:00:00");
			((Element) el).setAttribute("date",
					formater.format(eb.getCal().getTime()));
			rootNode.appendChild(el);
			updateelement = null;
			parentelement = el;

		}
		if (updateelement != null)
			el.replaceChild(eb.createElement(doc, false), updateelement);
		else
			el.appendChild(eb.createElement(doc, false));

		saveXml(Globals.timerecordfile, doc);
	}

	public void removeTimerecord(TimerecordBean eb) throws IOException,
			ParserConfigurationException, SAXException,
			TransformerConfigurationException, TransformerException {

		Element updateelement = null;
		Element parentelement = null;
		Element el = null;

		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		NodeList els = data.getElementsByTagName("dailySheet");
		Log.v("REMOVE", "before " + els.getLength());
		if (els.getLength() == 0) {
			return;
		} else {
			Log.v("REMOVE", "before for ");
			for (int j = 0; j < els.getLength(); j++) {
				Log.v("REMOVE", "in for ");
				try {
					el = (Element) els.item(j);
				} catch (Exception e) {
					continue;
				}
				NodeList eelements = el.getChildNodes();
				for (int i = 0; i < eelements.getLength(); i++) {
					Log.v("REMOVE", "in for for ");
					Element eelement;
					try {
						eelement = (Element) eelements.item(i);
					} catch (Exception e) {
						continue;
					}
					if (eb.getId() == 0) {
						Log.v("REMOVE", "in for for id 0 ");
						if (eelement.getAttribute("extId").equals(
								"" + eb.getExtId())) {
							Log.v("REMOVE", "in for for found extId ");
							updateelement = eelement;
							parentelement = el;
							break;
						}
					} else {
						Log.v("REMOVE", "in for for id > 0 ");
						if (eelement.getAttribute("id").equals("" + eb.getId())) {
							Log.v("REMOVE", "in for for found id ");
							updateelement = eelement;
							parentelement = el;
							break;
						}
					}
				}
				if (updateelement != null)
					break;
			}
		}
		if (updateelement != null) {
			parentelement.removeChild(updateelement);
			Log.v("REMOVE", "found ");
		} else {
			Log.v("REMOVE", "not found ");

		}

		saveXml(Globals.datafile, data);

		updateelement = null;
		el = null;
		File file = context.getFileStreamPath(Globals.timerecordfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		Node rootNode;

		dBuilder = dbFactory.newDocumentBuilder();
		if (file.exists()) {
			doc = dBuilder.parse((InputStream) context
					.openFileInput(Globals.timerecordfile));
			rootNode = doc.getElementsByTagName("flapps").item(0);

			els = doc.getElementsByTagName("dailySheet");
			for (int j = 0; j < els.getLength(); j++) {
				el = (Element) els.item(j);
				NodeList eelements = el.getChildNodes();
				for (int i = 0; i < eelements.getLength(); i++) {
					Element eelement = (Element) eelements.item(i);
					if (eb.getId() == 0) {
						if (eelement.getAttribute("extId").equals(
								"" + eb.getExtId()))
							updateelement = eelement;
					} else {
						if (eelement.getAttribute("id").equals(
								"" + eb.getExtId()))
							updateelement = eelement;

					}
				}
				if (el.getAttribute("date").equals(
						formater.format(eb.getCal().getTime())))
					parentelement = (Element) els.item(j);
			}
			if (parentelement == null) {
				NodeList nls = doc.getElementsByTagName("flapps");
				Node nl = nls.item(0);
				el = doc.createElement("dailySheet");
				((Element) el).setAttribute("startTime", "00:00:00");
				((Element) el).setAttribute("date",
						formater.format(eb.getCal().getTime()));
				nl.appendChild(el);
				updateelement = null;
				parentelement = el;
			}
		} else {
			doc = dBuilder.newDocument();
			rootNode = doc.createElement("flapps");
			doc.appendChild(rootNode);
			el = doc.createElement("dailySheet");
			((Element) el).setAttribute("startTime", "00:00:00");
			((Element) el).setAttribute("date",
					formater.format(eb.getCal().getTime()));
			rootNode.appendChild(el);
			updateelement = null;
			parentelement = el;

		}
		if (updateelement != null)
			el.removeChild(updateelement);
		else
			el.appendChild(eb.createElement(doc, true));

		saveXml(Globals.timerecordfile, doc);
	}

	public void storeUser(String user) throws IOException {
		FileOutputStream fos = context.openFileOutput(Globals.user,
				Context.MODE_PRIVATE);
		fos.write(user.getBytes());
		fos.close();
	}

	public JSONObject readUser() throws IOException {
		File file = context.getFileStreamPath(Globals.user);
		if (file.exists()) {
			Object obj = JSONValue.parse(IOUtils.toString((InputStream) context
					.openFileInput(Globals.user)));
			return (JSONObject) obj;
		}
		return null;
	}

	public void storeCheckinout(JSONObject checkinout)
			throws FileNotFoundException, IOException,
			TransformerConfigurationException, TransformerException {
		JSONArray existing = readCheckinout();
		existing.add(checkinout);
		saveJSON(Globals.checkinout, existing);
	}

	public JSONArray readCheckinout() throws IOException {
		File file = context.getFileStreamPath(Globals.checkinout);
		if (file.exists()) {
			Object obj = JSONValue.parse(IOUtils.toString((InputStream) context
					.openFileInput(Globals.checkinout)));
			return (JSONArray) obj;
		}
		return new JSONArray();
	}
	
	public void storeAttendance( JSONArray attendance ) throws IOException, ClassNotFoundException {
		ArrayList<ClockInOutBean> list = new ArrayList<ClockInOutBean>();
		for ( int i=0; i< attendance.size(); i++ ) {
			list.add( new ClockInOutBean((JSONObject)attendance.get(i)));
		}
		
		saveObject( list, "attendance.dat" );
		Log.v( "ATENDANCE", "Pocet API: "+list.size());
		
		Collections.sort(list);
		HashMap<String, ClockInOutBean> state = new HashMap<String, ClockInOutBean>();
		for ( int i = 0; i< list.size(); i++ ) {
			if ( state.get( list.get(i).getCardId() ) == null ) {
				state.put(list.get(i).getCardId(), list.get(i));
			}
			addAttendanceHistory( list.get(i) );
		}
		saveObject( state, "attendance-state.dat" );
	}
	
	public void addAttendance( ClockInOutBean attendance ) throws IOException, ClassNotFoundException {
		
		ArrayList<ClockInOutBean> list = readAttendance();
		Log.v( "ATENDANCE", "Pocet: "+list.size());
		list.add(attendance);
		
		saveObject( list, "attendance.dat" );
		Log.v( "ATENDANCE", "Pocet: "+list.size());
	}
	
	public ArrayList<ClockInOutBean> readAttendance() throws IOException, ClassNotFoundException {
		
		return (ArrayList<ClockInOutBean>) readObject( "attendance.dat" );
	}

	public void addAttendanceHistory( ClockInOutBean attendance ) throws IOException, ClassNotFoundException {
		
		ArrayList<ClockInOutBean> list = new ArrayList<ClockInOutBean>();
		try {
			list = readAttendanceHistory();
		} catch( Exception e ) {}
		Log.v( "ATENDANCE", "Pocet: "+list.size());
		list.add(attendance);
		
		saveObject( list, "attendance-history.dat" );
		Log.v( "ATENDANCE", "Pocet: "+list.size());
	}
	
	public ArrayList<ClockInOutBean> readAttendanceHistory() throws IOException, ClassNotFoundException {
		
		return (ArrayList<ClockInOutBean>) readObject( "attendance-history.dat" );
	}

	public void addUpdateAttendance( ClockInOutBean attendance ) throws IOException, ClassNotFoundException {
		ArrayList<ClockInOutBean> list = new ArrayList<ClockInOutBean>();
		try {
			list = readUpdateAttendance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list.add(attendance);
		
		saveObject( list, "attendance-update.dat" );
	}
	
	public void resetUpdateAttendance() throws IOException, ClassNotFoundException {
		ArrayList<ClockInOutBean> list = new ArrayList<ClockInOutBean>();
		saveObject( list, "attendance-update.dat" );
	}
	
	public ArrayList<ClockInOutBean> readUpdateAttendance() throws IOException, ClassNotFoundException {
		
		return (ArrayList<ClockInOutBean>) readObject( "attendance-update.dat" );
	}
	
	public void storeAttendanceStateValues( JSONObject obj ) throws IOException, ClassNotFoundException {
		
		saveObject(obj, "attendance-state-values.dat");
	}
	
	public JSONObject readAttendanceStateValues( ) throws IOException, ClassNotFoundException {
		return (JSONObject)readObject("attendance-state-values.dat");
	}
	
	public void storeAttendanceState( ClockInOutBean obj ) throws IOException, ClassNotFoundException {
		
		HashMap<String, ClockInOutBean> states = new HashMap<String, ClockInOutBean>();
		try {
			states = (HashMap<String, ClockInOutBean>)readObject("attendance-state.dat");
		} catch (Exception e){e.printStackTrace();}
		states.put(obj.getCardId(), obj);
		saveObject(states, "attendance-state.dat");
	}
	
	public ClockInOutBean readAttendanceState( String card ) throws IOException, ClassNotFoundException {
		HashMap<String, ClockInOutBean> states = new HashMap<String, ClockInOutBean>();
		try {
			states = (HashMap<String, ClockInOutBean>)readObject("attendance-state.dat");
		} catch (Exception e){e.printStackTrace();}
		return states.get( card );
	}
	
	public void saveCardName( ClockInOutBean ciob ) throws IOException, ClassNotFoundException {
		HashMap<String, String> map = (HashMap<String, String>) readObject("card-names.dat");
		String name = ciob.getName();
		if ( name != null) {
			if ( !"".equals(name) ) {
				map.put(ciob.getCardId(), ciob.getName());
			}
		}
	}
	
	public String readCardName( String card ) throws IOException, ClassNotFoundException {
		HashMap<String, String> map = (HashMap<String, String>) readObject("card-names.dat");
		return map.get(card);
	}

	public void storeCheckstate(String id) throws FileNotFoundException,
			IOException, TransformerConfigurationException, TransformerException {

		SimpleDateFormat sdf = (SimpleDateFormat)SimpleDateFormat.getInstance();
		sdf.applyPattern("yyyy-MM-dd");
		String today = sdf.format(new Date());
		
		JSONObject ids = new JSONObject();
		try {
			ids = readCheckstate(id);
		} catch( Exception e ) {
			ids = new JSONObject();
		}
		
		JSONObject item = (JSONObject) ids.get(id);
		if (item != null) {
			String itemvalue = (String) item.get(today);
			if (itemvalue == null)
				item.put(today, "on");
			else if ("on".equals(itemvalue))
				item.put(today, "off");
			else
				item.put(today, "on");
		} else {
			JSONObject newitem = new JSONObject();
			newitem.put(today, "on");
			ids.put(id, newitem);
		}
		saveJSON(Globals.checkstate, ids);
	}

	public void storeComeInOutState( HashMap<Long, Long> store ) throws IOException {

			saveObject( store, "comeinoitstate.dat");
	}

	public HashMap<Long, Long> readComeInOutState() throws IOException, ClassNotFoundException {

		return (HashMap<Long, Long>) readObject( "comeinoitstate.dat" );
	}

	public void storeComeInOutWork(Work work) throws IOException {

		saveObject( work, "comeinoutwork.dat" );
	}

	public Work readComeInOutWork() throws IOException, ClassNotFoundException {

		return (Work) readObject( "comeinoutwork.dat" );
	}

	public JSONObject readCheckstate(String id) throws FileNotFoundException,
			IOException {

		File file = context.getFileStreamPath(Globals.checkstate);
		if (file.exists()) {
			Object obj = JSONValue.parse(IOUtils.toString((InputStream) context
					.openFileInput(Globals.checkstate)));
			return (JSONObject) obj;
		}
		return new JSONObject();
	}

	public boolean isChecked(String id) throws FileNotFoundException,
			IOException {

		SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
		sdf.applyPattern("yyyy-MM-dd");
		String today = sdf.format(new Date());

		JSONObject ids = readCheckstate(id);
		JSONObject item = (JSONObject) ids.get(id);
		if (item != null) {
			String itemvalue = (String) item.get(today);
			if (itemvalue == null)
				return false;
			if ("on".equals(itemvalue))
				return true;
		}
		return false;
	}

	public void saveXml(String file, Document data) throws IOException,
			TransformerConfigurationException, TransformerException {

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(data), new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");

		FileOutputStream fos = context.openFileOutput(file,
				Context.MODE_PRIVATE);
		fos.write(output.getBytes());
		fos.close();
	}

	public void saveJSON(String file, JSONArray array) throws IOException,
			TransformerConfigurationException, TransformerException {

		StringWriter out = new StringWriter();
		array.writeJSONString(out);
		
		saveJSON(file, out);
	}

	public void saveJSON(String file, JSONObject array) throws IOException,
			TransformerConfigurationException, TransformerException {

		StringWriter out = new StringWriter();
		array.writeJSONString(out);
		
		saveJSON(file, out);
	}

	public void saveJSON(String file, StringWriter out) throws IOException,
			TransformerConfigurationException, TransformerException {

		String jsonText = out.toString();

		FileOutputStream fos = context.openFileOutput(file,
				Context.MODE_PRIVATE);
		fos.write(jsonText.getBytes());
		fos.close();
	}

	public ExpenseTypeHashMap getExpenceTypes() {
		return new ExpenseTypeHashMap(data);
	}

	public AbsenceRequestHashMap getAbsenceRequests() {
		return new AbsenceRequestHashMap(data);
	}

	public AbsenceTypeHashMap getAbsenceTypes() {
		return new AbsenceTypeHashMap(data);
	}

}
