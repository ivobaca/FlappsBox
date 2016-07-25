package com.flapps.mobile.android.flapps.data;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.flapps.mobile.android.flapps.ClockInOutActivity;
import com.flapps.mobile.android.flapps.IntroActivity;
import com.flapps.mobile.android.flapps.MainActivity;
import com.flapps.mobile.android.flapps.SyncActivity;
import com.flapps.mobile.android.flapps.config.AppConfig;
import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NfcSync extends SyncModel {

	private DataModel dm;
	public ClockInOutActivity activity;

	public NfcSync(Context context) {
		super(context);
		dm = new DataModel(context);
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

		// Toast.makeText(context, "1 "+actionTag, Toast.LENGTH_SHORT).show();

		try {

			linkhost = AppConfig.getApiUrl();
			url = new URL(linkhost + "/" + Globals.domain + "/rest/attendance");
			linkhost = AppConfig.getApiUrlFallback();
			fallback = new URL(linkhost + "/" + Globals.domain
					+ "/rest/attendance");

			ArrayList<ClockInOutBean> updates = new ArrayList<ClockInOutBean>();
			try {

				updates = dm.readUpdateAttendance();
			} catch (Exception e) {

			}

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
					postdata.put("state", dm.readAttendanceStateValues());
					String uattendance = send(url, fallback,
							postdata.toJSONString(), COOKIES_SEND, "POST",
							"application/json");
Log.v( "ATTENDANCE", postdata.toJSONString() );
					dm.resetUpdateAttendance();
				}
			} catch (Exception e) {
			}

			if ( !"sendonly".equals(params[0]) ) {
				Log.v("Sync attendance", "IN");
				JSONObject postdata = new JSONObject();
				postdata.put("state", dm.readAttendanceStateValues());
				String attendance = send(url, fallback, /*postdata.toJSONString()*/"", COOKIES_SEND, "POST",
						"application/json");
				if (attendance != null) {
					JSONObject attendance_json = (JSONObject) JSONValue
							.parse(attendance);
					if (attendance_json != null) {
						dm.storeAttendance((JSONArray) attendance_json
								.get("updates"));
						JSONObject state_json = (JSONObject)attendance_json.get("state");
						dm.storeAttendanceStateValues(state_json);
					}
				}
				Log.v("CONFIG", attendance);
			}


		} catch (Exception e) {
			e.printStackTrace();
			String err = e.getMessage();
			Log.v("Sync nfc", " holt error " + e.getMessage());
			return "";// e.getMessage();
		}
		
		return returnval;
	}

	@Override
	protected void onPostExecute(String result) {
		Globals.tempdata = result;
		if ( activity != null ) {
			activity.refreshList();
		}
	}

}
