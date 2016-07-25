package com.flapps.mobile.android.flapps.adapters;

import com.flapps.mobile.android.flapps.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

	public class TimerecordListAdapter extends ArrayAdapter<String>{

	    Context context;
	    int layoutResourceId;   
	    String data[] = null;
	   
	    public TimerecordListAdapter(Context context, int layoutResourceId, String[] data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        StringHolder holder = null;
	       
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	           
	            holder = new StringHolder();
	            holder.txtTitle = (TextView)row.findViewById(R.id.main_list_item_label);
	           
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (StringHolder)row.getTag();
	        }
	       
	        String string = data[position];
	        holder.txtTitle.setText(string);
	       
	        return row;
	    }
	   
	    static class StringHolder
	    {
	        TextView txtTitle;
	    }
	
}
