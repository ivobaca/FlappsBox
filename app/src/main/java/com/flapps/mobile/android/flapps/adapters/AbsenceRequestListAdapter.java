package com.flapps.mobile.android.flapps.adapters;

import com.flapps.mobile.android.flapps.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flapps.mobile.android.flapps.data.AbsenceRequestBean;

	public class AbsenceRequestListAdapter extends ArrayAdapter<AbsenceRequestBean>{

	    Context context;
	    int layoutResourceId;   
	    ArrayList<AbsenceRequestBean> data = null;
	   
	    public AbsenceRequestListAdapter(Context context, int layoutResourceId, ArrayList<AbsenceRequestBean> data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        ItemHolder holder = null;
	       
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	           
	            holder = new ItemHolder();
	            holder.txtTitle = (TextView)row.findViewById(R.id.absencerequest_list_item_title);
	            holder.txtDescription = (TextView)row.findViewById(R.id.absencerequest_list_item_description);
	            holder.txtDate = (TextView)row.findViewById(R.id.absencerequest_list_item_date);
	            holder.imgDot = (ImageView)row.findViewById(R.id.absencerequest_list_item_image);
	           
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (ItemHolder)row.getTag();
	        }
	       
	        AbsenceRequestBean trb = data.get(position);

	        if ( trb != null ) {
	        	holder.txtTitle.setText( trb.getName() );

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
				
				if (trb.getStartDate() != null && trb.getEndDate() != null) {
		        	holder.txtTitle.setText( trb.getName() );
		        	holder.txtDescription.setText( trb.getNote() );
		        	holder.txtDate.setText( sdf.format( trb.getStartDate().getTime() ) + "-" + sdf.format( trb.getEndDate().getTime() ) );
		        	if ( "approved".equals( trb.getStatus() ) )
		        		holder.imgDot.setImageResource(R.drawable.dot_green);
		        	else holder.imgDot.setImageResource(R.drawable.dot_red);
		        }
	        }
	        return row;
	    }
	   
	    static class ItemHolder
	    {
	        TextView txtTitle;
	        TextView txtDescription;
	        TextView txtDate;
	        ImageView imgDot;
	    }
	
}
