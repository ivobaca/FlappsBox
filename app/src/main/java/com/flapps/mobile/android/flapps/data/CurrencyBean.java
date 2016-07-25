package com.flapps.mobile.android.flapps.data;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class CurrencyBean implements Serializable {

	protected String name;
	protected String code;
	protected boolean before = false;
	protected String sign;
	
	public CurrencyBean( JSONObject obj ) {
		
		if ( obj == null ) return;
		
		name = (String)obj.get( "name" );
		code = (String)obj.get( "code" );
		before = (Boolean)obj.get( "isBefore" );
		sign = (String)obj.get( "sign" );
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isBefore() {
		return before;
	}
	public void setBefore(boolean before) {
		this.before = before;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
