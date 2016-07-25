package com.flapps.mobile.android.flapps.data;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.flapps.mobile.android.flapps.IntroActivity;
import com.flapps.mobile.android.flapps.MainActivity;
import com.flapps.mobile.android.flapps.SyncActivity;
import com.flapps.mobile.android.flapps.config.ApiConfig;
import com.flapps.mobile.android.flapps.config.AppConfig;
import com.flapps.mobile.android.flapps.utils.Globals;
import com.flapps.mobile.android.flapps.utils.StringUtils;

public class DataSync extends SyncModel {

	public String actionTag = "";

	private DataModel dm; 
	
	public DataSync(Context cx) {
		super( cx );
		dm = new DataModel(cx);
	}

	@Override
	protected String doInBackground(String... params) {
		
		final ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork == null || !activeNetwork.isConnected()) {
			return "";
		}

		URL url;
		URL fallback;
		String linkhost = "";
		String urlParameters = "";
		InputStream inputStream;
		String returnval = "";
		
		//Toast.makeText(context, "1 "+actionTag, Toast.LENGTH_SHORT).show();
		
		actionTag = params[0];
		try {
			File file = null;

			// create account
			if ( "account".equals(actionTag) ) {
				String urlstring = String.format( "http://www.flapps.com/getajax2.php?name=%s&email=%s&phone=%s&surname=%s&companyName=%s&code=%s",
						Globals.firstname,
						Globals.login,
						Globals.phone,
						Globals.lastname,
						Globals.domain,
						Globals.domain
				);
				Log.v("Sync", "sending: "+urlstring );
				forceHttp = true;
				String retval = send( new URL(urlstring), new URL(urlstring), "", COOKIES_NO, "POST", "text/html");
				Log.v("Sync", "sent: "+retval );
				forceHttp = false;
				return retval;
			}
			
			
			//	get cookies
			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost + "/" + Globals.domain + "/external/getData");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost + "/" + Globals.domain
					+ "/external/getData");
			returnval = send(url, fallback, "", COOKIES_RECEIVE, "POST", null);

			
			// new API
			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost + "/" + Globals.domain
					+ "/rest/config");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost + "/" + Globals.domain
					+ "/rest/config");

			//if (Globals.cookies != null) {
				Log.v("Sync config", "IN");
				ignoreUntrustedHttps();
				String config = send(url, fallback, "", COOKIES_NO, "GET", "application/json");
Log.v("CONFIG", config);
				if( config != null ) {
					JSONObject config_json = (JSONObject) JSONValue.parse(config);
					if ( config_json != null  ) new ApiConfig(context).save( config_json );
				}
				Log.v( "CONFIG", config );
			//}

				
				linkhost = AppConfig.getApiUrl();
				url = new URL(linkhost + "/" + Globals.domain + "/rest/attendance");
				linkhost = AppConfig.getApiUrlFallback();
				fallback = new URL(linkhost + "/" + Globals.domain
						+ "/rest/attendance");

				ArrayList<ClockInOutBean> updates = new ArrayList<ClockInOutBean>();
				try {

					updates = dm.readUpdateAttendance();
					JSONArray updates_json = new JSONArray();
					Iterator<ClockInOutBean> it = updates.iterator();
					while (it.hasNext()) {
						ClockInOutBean ciob = it.next();
						updates_json.add(ciob.toJSON());
					}
					try {
						if (updates_json.size() > 0) {
							JSONObject postdata = new JSONObject();
							postdata.put("updates", updates_json);
							//postdata.put("state", dm.readAttendanceStateValues());
							String uattendance = send(url, fallback,
									postdata.toJSONString(), COOKIES_SEND, "POST",
									"application/json");
Log.v("ATTSYNC", url.getPath());
Log.v("ATTSYNC", postdata.toJSONString());
Log.v("ATTSYNC", uattendance);
							dm.resetUpdateAttendance();
						}
					} catch (Exception e) {
					}
				} catch (Exception e) {

				}


				
				//	get attendance records
				linkhost = AppConfig.getApiUrl();
				url = new URL(linkhost + "/" + Globals.domain
						+ "/rest/attendance");
				linkhost = AppConfig.getApiUrlFallback();
				fallback = new URL(linkhost + "/" + Globals.domain
						+ "/rest/attendance");

				Log.v("Sync attendance", "IN");
				String attendance = send(url, fallback, "", COOKIES_SEND, "POST", "application/json");
Log.v("ATTSYNC", attendance);
				if( attendance != null ) {
					//Globals.tempdata = attendance;
					JSONObject attendance_json = (JSONObject) JSONValue.parse(attendance);
					if ( attendance_json != null  ) {
						dm.storeAttendance( (JSONArray)attendance_json.get("updates"));
						JSONObject state_json = (JSONObject)attendance_json.get("state");
						dm.storeAttendanceStateValues(state_json);
					}
				}
				Log.v( "CONFIG", attendance );

			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost
					+ "/rest/activities");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost
					+ "/rest/activities");

			file = context.getFileStreamPath(Globals.activitiesfile);
			if (file.exists() && Globals.cookies != null) {
				Log.v("Sync activities", "IN");
				inputStream = (InputStream) context
						.openFileInput(Globals.activitiesfile);
				urlParameters = StringUtils.getStringFromInputStream(inputStream);
				//ret = urlParameters + " " + send(url, fallback, URLEncoder.encode(urlParameters, "UTF-8"));
				JSONArray acts = (JSONArray) JSONValue.parse(urlParameters);
				for ( int i=0; i < acts.size(); i++ ) {
					Log.v("Sync activities", "act "+i);
					JSONObject obj = (JSONObject) acts.get(i);
					long old = (Long)obj.get("id");
					Log.v("Sync activities", "before if ");
					if ( old <= 0 ) obj.remove("id");
					else url = new URL( url.toString()+"/"+obj.get("id") );
					Log.v("Sync activities", "before send ");
					String retval = send(url, fallback, obj.toJSONString(), COOKIES_SEND, "PUT", "application/json");
					Log.v("Sync activities", "before replace ");
					if ( retval != "" && old <= 0 ) dm.replaceActivityId( old, retval.trim() );
					Log.v("Sync activities", "after replace ");
					Log.v("Sync activities", obj.toJSONString()+" after "+retval+" end");
				}
			}

			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost
					+ "/rest/clients");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost
					+ "/rest/clients");

			file = context.getFileStreamPath(Globals.clientsfile);
			if (file.exists() && Globals.cookies != null) {
				Log.v("Sync clients", "IN");
				inputStream = (InputStream) context
						.openFileInput(Globals.clientsfile);
				urlParameters = StringUtils.getStringFromInputStream(inputStream);
				//ret = urlParameters + " " + send(url, fallback, URLEncoder.encode(urlParameters, "UTF-8"));
				JSONArray acts = (JSONArray) JSONValue.parse(urlParameters);
				for ( int i=0; i < acts.size(); i++ ) {
					Log.v("Sync clients", "act "+i);
					JSONObject obj = (JSONObject) acts.get(i);
					long old = (Long)obj.get("id");
					if ( old <= 0 ) obj.remove("id");
					else url = new URL( url.toString()+"/"+obj.get("id") );
					String retval = send(url, fallback, obj.toJSONString(), COOKIES_SEND, "PUT", "application/json");
					Log.v("Sync clients", "before replace ");
					if ( retval != "" && old <= 0 ) dm.replaceClientId( old, retval.trim() );
					Log.v("Sync clients", "after replace ");
					Log.v("Sync clients", obj.toJSONString()+" after "+retval+" end");
				}
			}

			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost
					+ "/rest/projects");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost
					+ "/rest/projects");

			file = context.getFileStreamPath(Globals.projectsfile);
			if (file.exists() && Globals.cookies != null) {
				Log.v("Sync projects", "IN");
				inputStream = (InputStream) context
						.openFileInput(Globals.projectsfile);
				urlParameters = StringUtils.getStringFromInputStream(inputStream);
				//ret = urlParameters + " " + send(url, fallback, URLEncoder.encode(urlParameters, "UTF-8"));
				JSONArray acts = (JSONArray) JSONValue.parse(urlParameters);
				for ( int i=0; i < acts.size(); i++ ) {
					Log.v("Sync projects", "act "+i);
					JSONObject obj = (JSONObject) acts.get(i);
					long old = (Long)obj.get("id");
					if ( old <= 0 ) obj.remove("id");
					else url = new URL( url.toString()+"/"+obj.get("id") );
					String retval = send(url, fallback, obj.toJSONString(), COOKIES_SEND, "PUT", "application/json");
					try {
						Log.v("Sync projects", "before replace ");
						if ( retval != "" && old <= 0 ) dm.replaceProjectId( old, retval.trim() );
						Log.v("Sync projects", "before replace ");
					} catch(Exception e) {}
					Log.v("Sync projects", obj.toJSONString()+" after "+retval+" end");
				}
			}
			
			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost
					+ "/rest/timeclock");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost
					+ "/rest/timeclock");

			file = context.getFileStreamPath(Globals.checkinout);
			if (file.exists() && Globals.cookies != null) {
				Log.v("Sync checkinout", "IN");
				inputStream = (InputStream) context
						.openFileInput(Globals.checkinout);
				urlParameters = StringUtils.getStringFromInputStream(inputStream);
				//ret = urlParameters + " " + send(url, fallback, URLEncoder.encode(urlParameters, "UTF-8"));
				JSONArray acts = (JSONArray) JSONValue.parse(urlParameters);
				for ( int i=0; i < acts.size(); i++ ) {
					Log.v("Sync checkinout", "act "+i);
					JSONObject obj = (JSONObject) acts.get(i);
					String retval = send(url, fallback, obj.toJSONString(), COOKIES_SEND, "PUT", "application/json");
					Log.v("Sync checkinout", obj.toJSONString()+" after "+retval+" end");
				}
			}
			
			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost
					+ "/rest/users/me");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost
					+ "/rest/users/me");
			String retval = send(url, fallback, null, COOKIES_SEND, "GET", "application/json");
			dm.storeUser(retval);
			
			
			//	old API

			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost + "/" + Globals.domain
					+ "/external/updateData");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost + "/" + Globals.domain
					+ "/external/updateData");

			file = context.getFileStreamPath(Globals.timerecordfile);
			if (file.exists()) {
				inputStream = (InputStream) context
						.openFileInput(Globals.timerecordfile);
				urlParameters = StringUtils.getStringFromInputStream(inputStream);
				Log.v("Sync timerecord", urlParameters);
				//ret = urlParameters + " " + send(url, fallback, URLEncoder.encode(urlParameters, "UTF-8"));
				send(url, fallback, urlParameters, COOKIES_NO, "POST", null);
			}

			file = context.getFileStreamPath(Globals.expensesfile);
			if (file.exists()) {
				inputStream = (InputStream) context
						.openFileInput(Globals.expensesfile);
				urlParameters = StringUtils.getStringFromInputStream(inputStream);
				//send(url, fallback, URLEncoder.encode(urlParameters, "UTF-8"));
				send(url, fallback, urlParameters, COOKIES_NO, "POST", null);
			}

			file = context.getFileStreamPath(Globals.absencesfile);
			if (file.exists()) {
				inputStream = (InputStream) context
						.openFileInput(Globals.absencesfile);
				urlParameters = StringUtils.getStringFromInputStream(inputStream);
				//send(url, fallback, URLEncoder.encode(urlParameters, "UTF-8"));
				send(url, fallback, urlParameters, COOKIES_NO, "POST", null);
			}

			//	reload
			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost + "/" + Globals.domain + "/external/getData");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost + "/" + Globals.domain
					+ "/external/getData");

			returnval = send(url, fallback, "", COOKIES_RECEIVE, "POST", null);
			
			MenuConfigBean mcb = new ApiConfig(context).getMenuConfig();
			ArrayList<String> menuitems = mcb.getItems();
			if ( menuitems.contains("attendanceChecker") ) returnval = "<?xml version=\"1.0\" encoding=\"utf-8\"><flapps></flapps>";

		} catch (Exception e) {
			e.printStackTrace();
			String err = e.getMessage();
			Log.v("Sync activities", " holt error "+e.getMessage());
			return "";//e.getMessage();
		}

		return returnval;
	}



	@Override
	protected void onPostExecute(String result) {

		if ( result.indexOf( "<html" ) != -1 ) result = "";
		
		if ( "login".equals(actionTag) ) {
			((IntroActivity) context).logged(result);
			return;
		} else if ( "syncnow".equals(actionTag) ) {
			((SyncActivity) context).afterReload(result);
			return;
		} else if ( "account".equals(actionTag) ) {
			//((AccountActivity) context).afterCreating(result);
			return;
		} else {
			
			if ( context instanceof IntroActivity) {
				((IntroActivity) context).logged(result);
				
			} else 
			((MainActivity) context).afterReload(result);
		}
	}

}
