package com.flapps.mobile.android.flapps.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

import com.flapps.mobile.android.flapps.AccountActivity;
import com.flapps.mobile.android.flapps.IntroActivity;
import com.flapps.mobile.android.flapps.MainActivity;
import com.flapps.mobile.android.flapps.SyncActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class RemoteLog {

	public static void l(Context cx, String text) {
		/*
		Log.v("RemoteLog", "l");
		try {
			FileOutputStream fos = cx.openFileOutput("remote.log",
					Context.MODE_PRIVATE | Context.MODE_APPEND);
			fos.write(text.getBytes());
			fos.write(((String) System.getProperty("line.separator"))
					.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.v("RemoteLog", ""+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v("RemoteLog", ""+e.getMessage());
			e.printStackTrace();
		}

		RemoteLog.s(cx);
		*/
	}

	static void s(Context cx) {

		Log.v("RemoteLog", "s");
		try {
			String text = IOUtils.toString((InputStream) cx
					.openFileInput("remote.log"));

			Sender sender = new Sender(cx);
			sender.setContext(cx);
			sender.execute(text);

		} catch (FileNotFoundException e) {
			Log.v("RemoteLog", ""+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v("RemoteLog", ""+e.getMessage());
			e.printStackTrace();
		}

	}

	public static void clean(Context cx) {
		try {
			FileOutputStream fos = cx.openFileOutput("remote.log",
					Context.MODE_PRIVATE);
			fos.write("".getBytes());
			fos.write(((String) System.getProperty("line.separator"))
					.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			Log.v("RemoteLog", ""+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v("RemoteLog", ""+e.getMessage());
			e.printStackTrace();
		}

	}

	static class Sender extends AsyncTask<String, Void, String> {

		private Context context;

		public Sender(Context context) {
			super();
			this.context = context;
		}

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(String... arg0) {

			String returnval = "";
			Log.v("RemoteLog Sender", "background");

			final ConnectivityManager conMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnected()) {

				String urlParameters = arg0[0];
				Log.v("RemoteLog Sender", "params "+urlParameters);
				URL url;
				try {
					Log.v("RemoteLog Sender", "try");
					url = new URL("http://www.wigwam.sk/remotelog/index.php?text=xxxx");
					// String authString = "flapps:key4flapps";
					// byte[] authEncBytes =
					// Base64.encode(authString.getBytes(),
					// Base64.URL_SAFE);
					// String authStringEnc = new String(authEncBytes);

					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();

					Log.v("RemoteLog Sender", "conn");
					// conn.setRequestProperty("Authorization", "Basic "
					// + authStringEnc);
					conn.setRequestMethod("POST");
					OutputStreamWriter writer = new OutputStreamWriter(
							conn.getOutputStream());

					Log.v("RemoteLog Sender", "stream");
					writer.write(urlParameters);
					writer.flush();

					Log.v("RemoteLog Sender", "write");
					String line;
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));

					while ((line = reader.readLine()) != null) {
						returnval += line + "\n";
					}
					Log.v("RemoteLog Sender", "read");
					writer.close();
					reader.close();
					Log.v("RemoteLog Sender", "close");

					RemoteLog.clean(context);
					Log.v("RemoteLog Sender", "cleaned");

				} catch (MalformedURLException e) {
					Log.v("RemoteLog", ""+e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					Log.v("RemoteLog", ""+e.getMessage());
					e.printStackTrace();
				}

			}

			return returnval;
		}

		@Override
		protected void onPostExecute(String result) {
				Log.v("RemoteLog", "Returned: " + result);
		}
	}

}
