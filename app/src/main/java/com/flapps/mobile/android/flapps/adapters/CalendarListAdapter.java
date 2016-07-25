package com.flapps.mobile.android.flapps.adapters;

import com.flapps.mobile.android.flapps.R;

import java.util.ArrayList;

import com.flapps.mobile.android.flapps.data.ActivityBean;
import com.flapps.mobile.android.flapps.data.ActivityHashMap;
import com.flapps.mobile.android.flapps.data.ClientBean;
import com.flapps.mobile.android.flapps.data.ClientHashMap;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.ProjectBean;
import com.flapps.mobile.android.flapps.data.ProjectHashMap;
import com.flapps.mobile.android.flapps.data.TimerecordBean;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

	public class CalendarListAdapter extends ArrayAdapter<TimerecordBean>{

	    Context context;
	    int layoutResourceId;   
	    ArrayList<TimerecordBean> data = null;
	   
	    public CalendarListAdapter(Context context, int layoutResourceId, ArrayList<TimerecordBean> data) {
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
	            holder.txtTitle = (TextView)row.findViewById(R.id.calendar_list_item_text);
	            holder.txtDesc = (TextView)row.findViewById(R.id.calendar_list_item_desc);
	            holder.txtDuration = (TextView)row.findViewById(R.id.calendar_list_item_duration);
	           
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (ItemHolder)row.getTag();
	        }
	       
	        TimerecordBean trb = data.get(position);
	        DataModel dm = new DataModel(context);
	        ActivityHashMap ahm = dm.getActivities();
	        ActivityBean ab = ahm.get( trb.activityId );
	        ProjectHashMap phm = dm.getProjects();
	        ProjectBean pb = phm.get( trb.projectId );
	        ClientBean cb = new ClientBean();
	        if (pb != null) {
	        	ClientHashMap chm = dm.getClients();
	        	cb = chm.get( pb.getClientId() );
	        }

	        if ( ab != null )
	        	holder.txtTitle.setText( ab.getName() );
	        
	        int hours = (int) Math.floor( (double)trb.duration / (double)60 );
	        int minutes = trb.duration - ( hours * 60 );
	        holder.txtDuration.setText(String.format("%02d", hours)
					+ ":" + String.format("%02d", minutes));
	        if (pb != null) holder.txtDesc.setText( pb.getName()+" ("+cb.getName()+")" );
	       
	        return row;
	    }
	   
	    static class ItemHolder
	    {
	        TextView txtTitle;
	        TextView txtDesc;
	        TextView txtDuration;
	    }
	
}
