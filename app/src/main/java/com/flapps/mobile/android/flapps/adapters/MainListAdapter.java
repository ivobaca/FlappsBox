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

	public class MainListAdapter extends ArrayAdapter<String>{

	    Context context;
	    int layoutResourceId;   
	    String data[] = null;
	    int checked = -1;
	   
	    public MainListAdapter(Context context, int layoutResourceId, String[] data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }

	    public MainListAdapter(Context context, int layoutResourceId, String[] data, int ch) {
	    	this( context, layoutResourceId, data);
	    	checked = ch;
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
	            holder.txtTitle = (TextView)row.findViewById(R.id.main_list_item_text);
	            holder.imgCHecked = (ImageView)row.findViewById(R.id.activities_billable_check);
	           
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (StringHolder)row.getTag();
	        }
	       
	        String string = data[position];
	        holder.txtTitle.setText(string);
	        if (holder.imgCHecked != null) {
	        	if (checked == position) holder.imgCHecked.setVisibility(View.VISIBLE);
	        	else holder.imgCHecked.setVisibility(View.INVISIBLE);
	        }
	       
	        return row;
	    }
	   
	    static class StringHolder
	    {
	        TextView txtTitle;
	        ImageView imgCHecked;
	    }
	
}
