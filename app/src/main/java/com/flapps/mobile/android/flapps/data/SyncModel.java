package com.flapps.mobile.android.flapps.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.flapps.mobile.android.flapps.config.AppConfig;
import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class SyncModel extends AsyncTask<String, Void, String> { 

	public static int COOKIES_NO = 0;
	public static int COOKIES_SEND = 1;
	public static int COOKIES_RECEIVE = 2;
	public static int COOKIES_SEND_RECEIVE = 3;

	protected boolean forceHttp = false;

	public Context context;
	
	public SyncModel( Context context ) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String send(URL url, URL fallback, String urlParameters, int what, String method, String contentType) {
		String returnval = "";
		System.setProperty("http.agent", ""); 
		String versionName = "undefined";
        try {
			versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {

			try {
				String authString = Globals.login + ":" + Globals.password;
				byte[] authEncBytes = Base64.encode(authString.getBytes(),
						Base64.URL_SAFE);
				String authStringEnc = new String(authEncBytes).trim();

				if (AppConfig.isApiProtocolHttps() && !forceHttp) {

					TrustManager[] trustAllCerts = new TrustManager[] { new BusinessIntelligenceX509TrustManager() };
					SSLContext sc;

					try {
						sc = SSLContext.getInstance("SSL");
					} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
						return "";
					}

					HostnameVerifier hv = new BusinessIntelligenceHostnameVerifier();

					try {
						sc.init(null, trustAllCerts,
								new java.security.SecureRandom());
					} catch (KeyManagementException keyManagementException) {

						return "";
					}

					HttpsURLConnection.setDefaultSSLSocketFactory(sc
							.getSocketFactory());
					HttpsURLConnection.setDefaultHostnameVerifier(hv);
					HttpsURLConnection conn = (HttpsURLConnection) url
							.openConnection();

					conn.setRequestProperty("Authorization", "Basic "
							+ authStringEnc);
					conn.setRequestProperty("User-Agent", "APP.ANDROID."
							+ versionName);
					conn.setRequestMethod(method);
					if (contentType!=null) {
						conn.setRequestProperty("Content-Type", contentType);
						conn.setRequestProperty("Accept", contentType);
					}
					if ( Globals.cookies != null && ( what==COOKIES_SEND || what==COOKIES_SEND_RECEIVE ) ) {
						Iterator<String> it = Globals.cookies.iterator();
						while( it.hasNext() ) {
							String line = it.next();
							conn.setRequestProperty("Cookie", line);
							Log.v("Sync Cookie", line);
						}
					}
					
					OutputStreamWriter writer = null;
					if ( !"GET".equals(method) ) {
						if ( urlParameters != null ) {
							writer = new OutputStreamWriter(
									conn.getOutputStream());
		
							writer.write(urlParameters);
							// Log.v("Save", urlParameters);
							writer.flush();
						}
					}
					

					String line;
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));

					while ((line = reader.readLine()) != null) {
						returnval += line + "\n";
					}

					if ( urlParameters != null && writer != null ) writer.close();
					reader.close();

					//conn.getErrorStream()
					//Log.v("Send error", ""+ StringUtils.getStringFromInputStream( conn.getErrorStream() ) );
					Log.v("Send response", ""+conn.getResponseCode());

					if ( what==COOKIES_RECEIVE || what==COOKIES_SEND_RECEIVE ) Globals.cookies = conn.getHeaderFields().get("Set-Cookie");
					//Log.v("DataConnector", "cookies: "+Globals.cookies.size()+" - "+Globals.cookies.get(0));
					conn.disconnect();
				} else {
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();

					conn.setRequestProperty("Authorization", "Basic "
							+ authStringEnc);
					conn.setRequestProperty("User-Agent", "APP.ANDROID."
							+ versionName);
					conn.setRequestMethod(method);
					conn.setDoOutput(true);
					if ( Globals.cookies != null && ( what==COOKIES_SEND || what==COOKIES_SEND_RECEIVE ) ) {
						Iterator<String> it = Globals.cookies.iterator();
						while( it.hasNext() ) {
							String line = it.next();
							conn.setRequestProperty("Cookie", line);
							Log.v("Sync Cookie", line);
						}
					}
					OutputStreamWriter writer = null;
					//if (false ) {
					if ( urlParameters != null ) {
						writer = new OutputStreamWriter(
								conn.getOutputStream());
	
						writer.write(urlParameters);
						// Log.v("Save", urlParameters);
						writer.flush();
					}

					String line;
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));

					while ((line = reader.readLine()) != null) {
						returnval += line + "\n";
					}
					if ( urlParameters != null ) writer.close();
					reader.close();

					if ( what==COOKIES_RECEIVE || what==COOKIES_SEND_RECEIVE ) Globals.cookies = conn.getHeaderFields().get("Set-Cookie");
					conn.disconnect();
			}

			} catch (IOException e) {

				try {

					String authString = Globals.login + ":" + Globals.password;
					byte[] authEncBytes = Base64.encode(authString.getBytes(),
							Base64.URL_SAFE);
					String authStringEnc = new String(authEncBytes);

					if (AppConfig.isApiProtocolHttps() && !forceHttp) {

						TrustManager[] trustAllCerts = new TrustManager[] { new BusinessIntelligenceX509TrustManager() };
						SSLContext sc;

						try {
							sc = SSLContext.getInstance("SSL");
						} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
							return "";
						}

						HostnameVerifier hv = new BusinessIntelligenceHostnameVerifier();

						try {
							sc.init(null, trustAllCerts,
									new java.security.SecureRandom());
						} catch (KeyManagementException keyManagementException) {

							return "";
						}

						HttpsURLConnection.setDefaultSSLSocketFactory(sc
								.getSocketFactory());
						HttpsURLConnection.setDefaultHostnameVerifier(hv);
						HttpsURLConnection conn = (HttpsURLConnection) url
								.openConnection();

						conn.setRequestProperty("Authorization", "Basic "
								+ authStringEnc);
						conn.setRequestProperty("User-Agent", "APP.ANDROID."
								+ versionName);
						conn.setRequestMethod(method);
						if ( Globals.cookies != null && ( what==COOKIES_SEND || what==COOKIES_SEND_RECEIVE ) ) {
							Iterator<String> it = Globals.cookies.iterator();
							while( it.hasNext() ) {
								String line = it.next();
								conn.setRequestProperty("Cookie", line);
								Log.v("Sync Cookie", line);
							}
						}
						OutputStreamWriter writer = null;
						if ( urlParameters != null ) {
							writer = new OutputStreamWriter(
									conn.getOutputStream());
		
							writer.write(urlParameters);
							// Log.v("Save", urlParameters);
							writer.flush();
						}

						String line;
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(conn.getInputStream()));

						while ((line = reader.readLine()) != null) {
							returnval += line + "\n";
						}
						if ( urlParameters != null ) writer.close();
						reader.close();

						if ( what==COOKIES_RECEIVE || what==COOKIES_SEND_RECEIVE ) Globals.cookies = conn.getHeaderFields().get("Set-Cookie");
						conn.disconnect();
					} else {
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();

						conn.setRequestProperty("Authorization", "Basic "
								+ authStringEnc);
						conn.setRequestProperty("User-Agent", "APP.ANDROID."
								+ versionName);
						conn.setRequestMethod(method);
						if ( Globals.cookies != null && ( what==COOKIES_SEND || what==COOKIES_SEND_RECEIVE ) ) {
							Iterator<String> it = Globals.cookies.iterator();
							while( it.hasNext() ) {
								String line = it.next();
								conn.setRequestProperty("Cookie", line);
								Log.v("Sync Cookie", line);
							}
						}
						OutputStreamWriter writer = null;
						if ( urlParameters != null ) {
							writer = new OutputStreamWriter(
									conn.getOutputStream());
		
							writer.write(urlParameters);
							// Log.v("Save", urlParameters);
							writer.flush();
						}

						String line;
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(conn.getInputStream()));

						while ((line = reader.readLine()) != null) {
							returnval += line + "\n";
						}
						if ( urlParameters != null ) writer.close();
						reader.close();

						if ( what==COOKIES_RECEIVE || what==COOKIES_SEND_RECEIVE ) Globals.cookies = conn.getHeaderFields().get("Set-Cookie");
						conn.disconnect();
				}

				} catch (IOException e2) {

				}
			}
		}

		return returnval;

	}
	
	protected static void ignoreUntrustedHttps() throws Exception {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}
		};

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}
	
	class BusinessIntelligenceHostnameVerifier implements HostnameVerifier {

		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}

	}

	class BusinessIntelligenceX509TrustManager implements X509TrustManager {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
			// no-op
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
			// no-op
		}

	}

}