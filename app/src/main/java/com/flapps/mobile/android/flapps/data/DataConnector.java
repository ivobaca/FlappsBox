package com.flapps.mobile.android.flapps.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.flapps.mobile.android.flapps.AbsenceNewActivity;
import com.flapps.mobile.android.flapps.CalendarActivity;
import com.flapps.mobile.android.flapps.IntroActivity;
import com.flapps.mobile.android.flapps.LoginActivity;
import com.flapps.mobile.android.flapps.MainActivity;
import com.flapps.mobile.android.flapps.SyncActivity;
import com.flapps.mobile.android.flapps.config.AppConfig;
import com.flapps.mobile.android.flapps.utils.Globals;

public class DataConnector extends AsyncTask<String, Void, String> {

	public String actionTag = "";
	public Context context;
	public DataConnector( Context cx) {
		super();
		context = cx;
	}
	
	@Override
	protected String doInBackground(String... params) {
		actionTag = params[0];
		String urlParameters = "";
		try {
			urlParameters = URLEncoder.encode(params[1], "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		urlParameters = params[1];
Log.v("Saveing", urlParameters);
		String returnval = "";
		String linkhost = "";
		
		final ConnectivityManager conMgr =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			 linkhost = AppConfig.getApiUrl();
			try {
				URL url = new URL(linkhost+"/"+Globals.domain+"/external/getData");
				if ("saveActivity".equals(actionTag) || "removeActivity".equals(actionTag) || "saveExpense".equals(actionTag) || "saveAbsence".equals(actionTag) ) {
					 url = new URL(linkhost+"/"+Globals.domain+"/external/updateData");
				}
				
				String authString = Globals.login  + ":" + Globals.password;
				byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.URL_SAFE);
				String authStringEnc = new String(authEncBytes);
				
				if ( AppConfig.isApiProtocolHttps() ) {

					TrustManager[] trustAllCerts = new TrustManager[] { new  BusinessIntelligenceX509TrustManager() };
		            SSLContext sc;
		
		            try {
		                sc = SSLContext.getInstance("SSL");
		            }
		            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
		                return "";
		            }
		
		            HostnameVerifier hv = new BusinessIntelligenceHostnameVerifier();
		
		            try {
		                sc.init(null, trustAllCerts, new java.security.SecureRandom());
		            }
		            catch (KeyManagementException keyManagementException) {
		
		                return "";
		            }

		            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		            HttpsURLConnection.setDefaultHostnameVerifier(hv);
					HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

					conn.setRequestProperty ("Authorization", "Basic "+authStringEnc);
					conn.setRequestMethod("POST");
					OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			
					writer.write(urlParameters);
					//Log.v("Save", urlParameters);
					writer.flush();
			
					String line;
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
					while ((line = reader.readLine()) != null) {
						returnval += line+"\n";
					}
					//Log.v("Save", returnval);
					writer.close();
					reader.close();
					List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
					Log.v("DataConnector", "cookies: "+cookies.size());
//					conn.setRequestProperty("Cookie", "name1=value1; name2=value2");

				} else {
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();

					conn.setRequestProperty ("Authorization", "Basic "+authStringEnc);
					conn.setRequestMethod("POST");
					OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			
					writer.write(urlParameters);
					writer.flush();
			
					String line;
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
					while ((line = reader.readLine()) != null) {
						returnval += line+"\n";
					}
					writer.close();
					reader.close();
	//				List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
				}
				
			} catch(IOException e) {
				linkhost = AppConfig.getApiUrlFallback();
				try {
					URL url = new URL(linkhost+"/"+Globals.domain+"/external/getData");
					if ("saveActivity".equals(actionTag) || "removeActivity".equals(actionTag) || "saveExpense".equals(actionTag) || "saveAbsence".equals(actionTag) ) {
						 url = new URL(linkhost+"/"+Globals.domain+"/external/updateData");
					}
					
					String authString = Globals.login  + ":" + Globals.password;
					byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.URL_SAFE);
					String authStringEnc = new String(authEncBytes);
					
					if ( AppConfig.isApiProtocolHttps() ) {

						TrustManager[] trustAllCerts = new TrustManager[] { new  BusinessIntelligenceX509TrustManager() };
			            SSLContext sc;
			
			            try {
			                sc = SSLContext.getInstance("SSL");
			            }
			            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			                return "";
			            }
			
			            HostnameVerifier hv = new BusinessIntelligenceHostnameVerifier();
			
			            try {
			                sc.init(null, trustAllCerts, new java.security.SecureRandom());
			            }
			            catch (KeyManagementException keyManagementException) {
			
			                return "";
			            }

			            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			            HttpsURLConnection.setDefaultHostnameVerifier(hv);
						HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

						conn.setRequestProperty ("Authorization", "Basic "+authStringEnc);
						conn.setRequestMethod("POST");
						OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
				
						writer.write(urlParameters);
						writer.flush();
				
						String line;
						BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
						while ((line = reader.readLine()) != null) {
							returnval += line+"\n";
						}
						writer.close();
						reader.close();
					} else {
						HttpURLConnection conn = (HttpURLConnection)url.openConnection();

						conn.setRequestProperty ("Authorization", "Basic "+authStringEnc);
						conn.setRequestMethod("POST");
						OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
				
						writer.write(urlParameters);
						writer.flush();
				
						String line;
						BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
						while ((line = reader.readLine()) != null) {
							returnval += line+"\n";
						}
						writer.close();
						reader.close();
					}
					
				} catch(IOException e2) {
					
				}
			}
		}
		
		return returnval;
	}
	

	@Override
	protected void onPostExecute(String result) { 
		if ("login".equals(actionTag))
			((LoginActivity) context).logged( result );
		if ("loginintro".equals(actionTag))
			((IntroActivity) context).logged( result );
		if ("saveActivity".equals(actionTag)) {
			if ( "".equals(result) ) {
				((MainActivity) context).afterSaveFailed( result );
				return;
			}
			((MainActivity) context).afterSave( getStatus(result) );
		}
		if ("removeActivity".equals(actionTag)) {

			FileOutputStream fos2;
			try {
				fos2 = context.openFileOutput("debug", Context.MODE_PRIVATE);
				fos2.write( result.getBytes() );
				fos2.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			
			if ( "".equals(result) ) return;
			((CalendarActivity) context).afterRemove( getStatus(result) );
		}
		if ("reloadRemoved".equals(actionTag)) {
			if ( "".equals(result) ) return;
			((CalendarActivity) context).afterReload( result );
		}
		if ("reloadExpenses".equals(actionTag)) {
			//if ( "".equals(result) ) return;
			//((CostsNewActivity) context).afterReload( result );
		}
		if ("reloadAbsences".equals(actionTag)) {
			//if ( "".equals(result) ) return;
			((AbsenceNewActivity) context).afterReload( result );
		}
		
		if ("reload".equals(actionTag)) {
			if ( "".equals(result) ) return;
			((MainActivity) context).afterReload( result );
		} 
		if ("reloadSync".equals(actionTag)) {
			if ( "".equals(result) ) return;
			((SyncActivity) context).afterReload( result );
		} 
		if ("saveExpense".equals(actionTag)) {
			if ( "".equals(result) ) {
				//((CostsNewActivity) context).afterSaveFailed( result );
				return;
			}
			//((CostsNewActivity) context).afterSave( getStatus(result) );
		}
		if ("saveAbsence".equals(actionTag)) {
			if ( "".equals(result) ) {
				((AbsenceNewActivity) context).afterSaveFailed( result );
				return;
			}
			((AbsenceNewActivity) context).afterSave( getStatus(result) );
		}
	}
	
	public String getStatus( String result ) {

		String status = "none";
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			
			dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(result));
			Document doc = dBuilder.parse  ( is );
			Node updateResult = doc.getElementsByTagName("updateResult").item(0);
			if (updateResult != null) {
				NamedNodeMap nnm = updateResult.getAttributes();
				Node attr = nnm.getNamedItem("status");
				status = attr.getNodeValue();
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
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

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            // no-op
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            // no-op
        }

    }
}
