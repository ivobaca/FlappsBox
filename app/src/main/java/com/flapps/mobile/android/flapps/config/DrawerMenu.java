package com.flapps.mobile.android.flapps.config;

import com.flapps.mobile.android.flapps.R;

import com.flapps.mobile.android.flapps.data.MenuConfigBean;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;

public class DrawerMenu {

	public static String[] getArray( Context context ) {
		
		ArrayList<String> list = new ArrayList<String>();
		String[] fields = context.getResources().getStringArray(R.array.fields_array);
		try {
			MenuConfigBean mcb = new ApiConfig(context).getMenuConfig();
			ArrayList<String> menuitems = mcb.getItems();
			for ( int i=0; i< fields.length; i++ ) {
				if ( i == 0) {
					if ( menuitems.contains("tms") ) list.add( fields[i] );
				} else if ( i == 1) {
						if ( menuitems.contains("expense") ) list.add( fields[i] );
				}else if ( i == 2 ) {
					if ( menuitems.contains("absence") ) list.add( fields[i] );
				}else if ( i == 3 ) {
					if ( menuitems.contains("clockInOut") ) list.add( fields[i] );
				}else if ( i == 4 ) {
					if ( menuitems.contains("attendanceChecker") ) list.add( fields[i] );
				} else {
					list.add( fields[i] );
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] newfields = new String[list.size()];
		for ( int i=0; i< list.size(); i++ ) {
			newfields[i] = list.get(i);
		}
		return newfields;
	}
	
	public static Integer[] getMap( Context context ) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		String[] fields = context.getResources().getStringArray(R.array.fields_array);
		try {
			MenuConfigBean mcb = new ApiConfig(context).getMenuConfig();
			ArrayList<String> menuitems = mcb.getItems();
			for ( int i=0; i< fields.length; i++ ) {
				if ( i == 0) {
					if ( menuitems.contains("tms") ) list.add( i );
				} else if ( i == 1) {
					if ( menuitems.contains("expense") ) list.add( i );
				}else if ( i == 2 ) {
					if ( menuitems.contains("absence") ) list.add( i );
				}else if ( i == 4 ) {
					if ( menuitems.contains("attendanceChecker") ) list.add( i );
				}else if ( i == 3 ) {
					if ( menuitems.contains("clockInOut") ) list.add( i );
				} else {
					list.add( i );
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Integer[] newfields = new Integer[list.size()];
		for ( int i=0; i< list.size(); i++ ) {
			newfields[i] = list.get(i);
		}
		return newfields;
	}
}
