package com.flapps.mobile.android.flapps.data;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class ApiConfigBean implements Serializable {

	public final static int TMS_METHOD_DURATION = 0;
	
	protected String language;
	protected int tms_method = 0;
	protected boolean tms_showAmPm = false;
	protected boolean tms_forbidTimeEdit = false;
	protected boolean absence_denyAbsenceOverflow = false;
	protected CurrencyBean currency;
	
	public ApiConfigBean( JSONObject obj ) {
		language = (String)obj.get( "language" );
		String method = (String)obj.get("tms_method");
		if ( "duration".equals(method) ) tms_method = TMS_METHOD_DURATION;
		if ( obj.get( "tms_showAmPm" ) != null) tms_showAmPm = (Boolean)obj.get( "tms_showAmPm" );
		if ( obj.get( "tms_forbidTimeEdit" ) != null) tms_forbidTimeEdit = (Boolean)obj.get( "tms_forbidTimeEdit" );
		if ( obj.get( "absence_denyAbsenceOverflow" ) != null) absence_denyAbsenceOverflow = (Boolean)obj.get( "absence_denyAbsenceOverflow" );
		currency = new CurrencyBean((JSONObject) obj.get("currency"));
	}
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getTms_method() {
		return tms_method;
	}
	public void setTms_method(int tms_method) {
		this.tms_method = tms_method;
	}
	public boolean isTms_showAmPm() {
		return tms_showAmPm;
	}
	public void setTms_showAmPm(boolean tms_showAmPm) {
		this.tms_showAmPm = tms_showAmPm;
	}
	public boolean isTms_forbidTimeEdit() {
		return tms_forbidTimeEdit;
	}
	public void setTms_forbidTimeEdit(boolean tms_forbidTimeEdit) {
		this.tms_forbidTimeEdit = tms_forbidTimeEdit;
	}
	public boolean isAbsence_denyAbsenceOverflow() {
		return absence_denyAbsenceOverflow;
	}
	public void setAbsence_denyAbsenceOverflow(boolean absence_denyAbsenceOverflow) {
		this.absence_denyAbsenceOverflow = absence_denyAbsenceOverflow;
	}
	public CurrencyBean getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyBean currency) {
		this.currency = currency;
	}
	
	
}
