package com.flapps.mobile.android.flapps;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.simple.JSONObject;

import com.flapps.mobile.android.flapps.data.AppStatus;
import com.flapps.mobile.android.flapps.data.ClockInOutBean;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.DataSync;
import com.flapps.mobile.android.flapps.data.NfcSync;
import com.flapps.mobile.android.flapps.utils.Beeper;
//import com.flapps.mobile.android.flapps.utils.Filmer;
//import com.flapps.mobile.android.flapps.utils.Foto;











import com.flapps.mobile.android.flapps.utils.Globals;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NFCCardActivity extends BasicActivity {
	
	private boolean doit = true;
    private long start = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        start= System.currentTimeMillis();
        Log.v( "NFCTIME", "start " + ( System.currentTimeMillis() - start ) );
        Log.v("NFCCardActivity", "create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcreceiver);
        getActionBar().hide();
        Log.v( "NFCTIME", "created " + ( System.currentTimeMillis() - start ) );

        if ( Globals.recentnfc == null ) Globals.recentnfc = new HashMap<String, Long>();
        Log.v( "NFCTIME", "created 2 " + ( System.currentTimeMillis() - start ) );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        Log.v( "NFCTIME", "resume start " + ( System.currentTimeMillis() - start ) );

        super.onResume();
        TextView tv = (TextView) findViewById(R.id.results_view);
        Intent intent = getIntent();
        Log.v( "NFCCardActivity", getIntent().getAction());

        Log.v( "NFCTIME", "resume intent " + ( System.currentTimeMillis() - start ) );
        /*
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String id = bytesToHex(tag.getId());
            Log.v( "NFCCardActivity", "ID (hex): "+id);
            if ( tag != null ) {
                Log.v( "NFCCardActivity", "tag");
                Log.v("NFCCardActivity", tag.getId().toString());
                String[] l = tag.getTechList();
                for ( int k=0; k < l.length; k++ ) {
                    Log.v("NFCCardActivity", "" +l[k]);
                }
                tv.setText( "ID: "+id+System.getProperty("line.separator")+"Data: "+readTag(tag) );
            } else Log.v( "NFCCardActivity", "tag null");

        }*/
        
        //new Filmer();
        //f =new Foto(this);
        if (doit) {
        	doit = false;
            Log.v( "NFCTIME", "resume doit " + ( System.currentTimeMillis() - start ) );

        	String action = getIntent().getAction();
            Log.v("DEBILNE eclips", action);
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
                Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (rawMsgs != null) {
                    Log.v("NFCReceiverActivity", "not null");

                    NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                    for (int i = 0; i < rawMsgs.length; i++) {
                        Log.v("NFCReceiverActivity", ".");
                        msgs[i] = (NdefMessage) rawMsgs[i];
                        NdefRecord[] records = msgs[i].getRecords();
                        String tvtext = "";
                        for ( NdefRecord record: records ) {
                        	String[] parts = new String (record.getPayload()).split("\\|");
//                        	tvtext += "id:"+parts[1]+System.getProperty("line.separator")+"User:"+parts[2]+System.getProperty("line.separator");

                        	try {
		                        String id = "-1";
	                        	if (parts.length > 1 ) id = parts[1];
	                        	boolean allowed = true;
	                        	if ( Globals.recentnfc.get(id) != null ) {
	                        		if ( Globals.recentnfc.get(id) + 10000 > new Date().getTime() )
	                        			allowed = false;
	                        	}
	                        	if ( allowed ) {
                                    Log.v( "NFCTIME", "resume allowed " + ( System.currentTimeMillis() - start ) );

                                    Globals.recentnfc.put(id, new Date().getTime());
		    	                    JSONObject obj = new JSONObject();
		    	                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		    	                    obj.put("created", sdf.format(new Date()));
		    	                    obj.put("cardId", id);

                                    Log.v( "NFCTIME", "resume format " + ( System.currentTimeMillis() - start ) );
		    	                    ClockInOutBean ciob = new ClockInOutBean();
		    	                    ciob.setCardId(id);
	//	    	                    ciob.setCardId("222");
		    	                    ciob.setTime(new Date().getTime());
		    	                    if ( parts.length > 2 )  ciob.setName(parts[2]);
                                    Log.v( "NFCTIME", "resume ciob " + ( System.currentTimeMillis() - start ) );

		    	                    DataModel dm = new DataModel(this);

                                    ClockInOutBean statebean;
                                    if ( Globals.statebeans == null ) Globals.statebeans = new HashMap<String, ClockInOutBean>();
                                    if ( Globals.statebeans.get( ciob.getCardId() ) == null ) {
                                        statebean = dm.readAttendanceState(ciob.getCardId());
                                    } else {
                                        statebean = Globals.statebeans.get( ciob.getCardId() );
                                    }
                                    Log.v( "NFCTIME", "resume statebean " + ( System.currentTimeMillis() - start ) );
	//	    	                    if ( dm.isChecked(id) ) ciob.setActivityId(-1l);
	//	    	                    else ciob.setActivityId(0l);
                                    Log.v("ATENDANCE", "statebean = before");
                                    if ( statebean == null ) {
                                        Log.v( "NFCTIME", "resume before beeper " + ( System.currentTimeMillis() - start ) );
                                        new Beeper(this);
                                        Log.v("ATENDANCE", "statebean = null");
                                    }
                                    else {
                                        if ( statebean.getActivityId() == 0l ) {
                                            Log.v("ATENDANCE", "statebean = 0");
                                            new Beeper(this);
                                        } else {
                                            Log.v("ATENDANCE", "statebean = -1");
                                            new Beeper(this, true);
                                        }
                                    }
                                    Log.v( "NFCTIME", "resume afyer beep " + ( System.currentTimeMillis() - start ) );

                                    if ( statebean == null ) ciob.setActivityId(-1l);
		    	                    else {
		    	                    	if ( statebean.getActivityId() == 0l ) ciob.setActivityId(statebean.getActivityId());
		    	                    	else ciob.setActivityId(0l);
		    	                    }
		    	                    
		    	                    dm.storeCheckinout(obj);
		    	                    dm.addAttendance(ciob);
		    	                    dm.addUpdateAttendance(ciob);
                                    Globals.statebeans.put( ciob.getCardId(), ciob );
		    	                    try {
		    	                    	dm.saveCardName(ciob);
		    	                    }catch(Exception ee) {}
		    	                    
		    	                    try {
		    	                    	dm.storeCheckstate(id);
		    	                    	dm.storeAttendanceState(ciob);
		    	                    } catch ( Exception e ) {}
	                        	}
                        	}catch( IOException ioe ) {} catch (TransformerConfigurationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (TransformerException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ClassNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
}
                        tv.setText(tvtext);

                    }
                } else Log.v("NFCReceiverActivity", "null");
            }
	        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
	
	            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	            String id = bytesToHex(tag.getId());
	            Log.v( "NFCCardActivity", "ID (hex): "+id);
	            if ( tag != null ) {
	            	NfcA nfca = NfcA.get(tag);
	                try{
	                	/*
	                    nfca.connect();
	                    Short s = nfca.getSak();
	                    
	                    byte[] a = nfca.getAtqa();
	                    String atqa = new String(a, Charset.forName("US-ASCII"));
	                    tv.setText("ID = "+id+"\nSAK = "+s+"\nATQA = "+atqa);
	                    nfca.close();
	                    */
	                	
	                	StringBuilder result = new StringBuilder();
	                    for (int i = 0; i <=id.length()-2; i=i+2) {
	                        result.append(new StringBuilder(id.substring(i,i+2)).reverse());
	                     }
	                	
	                	tv.setText("ID (hex) = "+id+"\nID (dec) = " + Integer.parseInt(result.reverse().toString(), 16 ) );
	                	
	                    JSONObject obj = new JSONObject();
	                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	                    obj.put("created", sdf.format(new Date()));
	                    obj.put("cardId", id);
	                    
	                    ClockInOutBean ciob = new ClockInOutBean();
	                    ciob.setCardId(id);
	                    ciob.setTime(new Date().getTime());
	                    
	                    DataModel dm = new DataModel(this);
	                    dm.storeCheckinout(obj);
	                    dm.addAttendance(ciob);
	                    dm.addUpdateAttendance(ciob);
	                    
	                    if ( !dm.isChecked(id) ) {
	                    	new Beeper(this);
	                    } else {
	                    	new Beeper(this, true);
	                    }
	                    try {
	                    	dm.storeCheckstate(id);
	                    } catch ( Exception e ) {}
	                }
	                catch(Exception e){
	                    Log.e("NFCCardActivity", "Error when reading tag");
	                    tv.setText("Error");
	                }
	             } else Log.v( "NFCCardActivity", "tag null");
	
	        }
	        
	        NfcSync nfcs = new NfcSync(NFCCardActivity.this);
			nfcs.execute("sendonly","");
			
	        if ( Globals.clockactivity != null )
	        	Globals.clockactivity.refreshList();
	        
	        Intent intent2 = new Intent(getApplicationContext(), ClockInOutActivity.class);
			//startActivity(intent2);
			NFCCardActivity.this.finish();
        }
}

    public String readTag(Tag tag) {
        MifareClassic mifare = MifareClassic.get(tag);
        String result = "";
        byte[] data;

        if ( mifare != null) {
            try {
                boolean auth = false;
                String cardData = null;
                mifare.connect();
                int secCount = mifare.getSectorCount();
                int bCount = 0;
                int bIndex = 0;
                for ( int j=0; j<secCount; j++ ) {
// 6.1) authenticate the sector
                    auth = mifare.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);
                    if(auth){
                        //if ( j==1) mifare.writeBlock(mifare.sectorToBlock(j), new String("aaaabbbbccccdddd").getBytes());
                        // 6.2) In each sector - get the block count
                        bCount = mifare.getBlockCountInSector(j);
                        bIndex = 0;
                        for(int i = 0; i < bCount; i++){
                            bIndex = mifare.sectorToBlock(j);
                            // 6.3) Read the block
                            data = mifare.readBlock(bIndex+i);
                            //result += convertHexToString( bytesToHex(data) ) + System.getProperty("line.separator");
                            if (j==1 && i==0) result = convertHexToString( bytesToHex(data) );
                            // 7) Convert the data into a string from Hex format.
                            Log.i("DATA", "S"+j+" B"+i+"  I"+bIndex+" "+bytesToHex(data));
                            bIndex++;
                        }
                    }else{ // Authentication failed - Handle it

                    }
                }
            } catch (IOException e) {
                Log.e("Mifare", "IOException while writing MifareClassic message...", e);
            } finally {
                if (mifare != null) {
                    try {
                        mifare.close();
                    } catch (IOException e) {
                        Log.e("Mifare", "Error closing tag...", e);
                    }
                }
            }
        }
        return result;
    }

    private String bytesToHex(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public String convertHexToString(String hex){

        String ascii="";
        String str;

        // Convert hex string to "even" length
        int rmd,length;
        length=hex.length();
        rmd =length % 2;
        if(rmd==1)
            hex = "0"+hex;

        // split into two characters
        for( int i=0; i<hex.length()-1; i+=2 ){

            //split the hex into pairs
            String pair = hex.substring(i, (i + 2));
            //convert hex to decimal
            //Log.v("PAIR", pair);
            int dec = Integer.parseInt(pair, 16);
            str=CheckCode(dec);
            ascii=ascii+" "+str;
        }
        return ascii;
    }

    public String CheckCode(int dec){
        String str;

        //convert the decimal to character
        str = Character.toString((char) dec);

        if(dec<32 || dec>126 && dec<161)
            str="-";
        return str;
    }


	@Override
	protected void onPause() {
		//f.close();
		super.onPause();
		if ( !isFinishing() ) finish();
	}
    
    
}
