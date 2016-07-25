package com.flapps.mobile.android.flapps.utils;

import com.flapps.mobile.android.flapps.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Beeper {

	private int duration = 1000; // miliseconds
    private int sampleRate = 8000;
    private int numSamples = (int)((duration / 1000) * sampleRate);
    private double sample[] = new double[numSamples];
    private double freqOfTone = 3900; // hz

    private byte[] generatedSnd = new byte[2 * numSamples];
    
    private AudioTrack audioTrack;
    private AudioTrack audioTrack1;
    private AudioTrack audioTrack2;
    private AudioTrack audioTrack3;

    Handler handler = new Handler();
    
    public static Camera cam = null;
    
    public Beeper( Context context ) {
        super();
        long start = System.currentTimeMillis();
        Log.v( "NFCTIME", "beeper start " + ( System.currentTimeMillis() - start ) );
		genTone();
        Log.v( "NFCTIME", "beeper gentone " + ( System.currentTimeMillis() - start ) );
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
		audioTrack.write(generatedSnd, 0, generatedSnd.length);
        Log.v( "NFCTIME", "beeper audio " + ( System.currentTimeMillis() - start ) );
		Lighter l = new Lighter(context);
        Log.v( "NFCTIME", "beeper lighter " + ( System.currentTimeMillis() - start ) );
		l.lightsON();
        Log.v( "NFCTIME", "beeper lighter on " + ( System.currentTimeMillis() - start ) );
		audioTrack.play();
        Log.v( "NFCTIME", "beeper lighter played " + ( System.currentTimeMillis() - start ) );
		SystemClock.sleep(300);
        Log.v( "NFCTIME", "beeper lighter sleeped " + ( System.currentTimeMillis() - start ) );
    	l.lightsOFF();
        Log.v( "NFCTIME", "beeper lighter off " + ( System.currentTimeMillis() - start ) );
    	l.release();
        Log.v( "NFCTIME", "beeper lighter released " + ( System.currentTimeMillis() - start ) );
    	//new Lighter(context).execute(false);
		//playSound();
	}

    public Beeper(Context context,boolean beeps) {
		super();
		genTones();
		audioTrack1 = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
		audioTrack1.write(generatedSnd, 0, generatedSnd.length);
		
		audioTrack2 = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
		audioTrack2.write(generatedSnd, 0, generatedSnd.length);
		
		audioTrack3 = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
		audioTrack3.write(generatedSnd, 0, generatedSnd.length);
		
    	Lighter l = new Lighter(context);
		l.lightsON();
		audioTrack1.play();
		//SystemClock.sleep(100);
    	//audioTrack.stop();
    	l.lightsOFF();
		SystemClock.sleep(200);
		l.lightsON();
		audioTrack2.play();
		//SystemClock.sleep(100);
    	//audioTrack.stop();
    	l.lightsOFF();
    	/*
		SystemClock.sleep(100);
		l.lightsON();
		audioTrack3.play();
		SystemClock.sleep(100);
    	//audioTrack.stop();
    	l.lightsOFF();
		SystemClock.sleep(100);
		*/
    	l.release();
    }

	void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples/3; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

	void genTones(){
        // fill out the array
        for (int i = 0; i < numSamples/10; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }
/*        for (int i = 0; i < numSamples/4; ++i) {
            sample[(numSamples/4 + numSamples/8)+i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }
        for (int i = 0; i < numSamples/4; ++i) {
            sample[(2*(numSamples/4) + 2*(numSamples/8))+i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }
*/
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    void playSound(){
         
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }
    
    void playBeep(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }
    

	public class Lighter extends AsyncTask<Boolean, Void, Void> {

		Context context = null;
		Parameters p_on;
		Parameters p_off;
		
		
		public Lighter(Context context) {
			super();
			this.context = context;
			if (context.getPackageManager().hasSystemFeature(
	                PackageManager.FEATURE_CAMERA_FLASH)) {
	            cam = Camera.open();
				//cam.unlock();
	            p_on = cam.getParameters();
	            p_on.setFlashMode(Parameters.FLASH_MODE_TORCH);
	            p_off = cam.getParameters();
	            p_off.setFlashMode(Parameters.FLASH_MODE_OFF);
	        }
		}
		
		@Override
		protected Void doInBackground(Boolean... arg0) {
			
			if ( arg0[0] ) lights();
			else light();
			
			return null;
		}
	    private void light() {
	    	lightsON();
			SystemClock.sleep(500);
	    	lightsOFF();
	    }
	    
	    private void lights() {
	    	lightsON();
			SystemClock.sleep(250);
	    	lightsOFF();
			SystemClock.sleep(125);
	    	lightsON();
			SystemClock.sleep(250);
	    	lightsOFF();
			SystemClock.sleep(125);
	    	lightsON();
			SystemClock.sleep(250);
	    	lightsOFF();
	    }
	    
		public void lightsON() {
			
			try {
		        if (context.getPackageManager().hasSystemFeature(
		                PackageManager.FEATURE_CAMERA_FLASH)) {
		        	if( cam == null ) {
			            cam = Camera.open();
		        	}
		        	cam.setParameters(p_on);
		            cam.startPreview();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		
		public void lightsOFF() {
			
		    try {
		        if (context.getPackageManager().hasSystemFeature(
		                PackageManager.FEATURE_CAMERA_FLASH)) {
		        	if (cam != null) {
			        	cam.setParameters(p_off);
			        	cam.stopPreview();
		        	}
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		
		public void release() {
			if ( cam != null ) {
				cam.lock();
				cam.release();
	            cam = null;
			}
		}
	}
}
