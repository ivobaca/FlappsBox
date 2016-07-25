package com.flapps.mobile.android.flapps.adapters;

import com.flapps.mobile.android.flapps.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.flapps.mobile.android.flapps.data.ActivityBean;
import com.flapps.mobile.android.flapps.data.ActivityHashMap;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.ExpenseBean;
import com.flapps.mobile.android.flapps.data.TimerecordBean;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

	public class CostsListAdapter extends ArrayAdapter<ExpenseBean>{

	    Context context;
	    int layoutResourceId;   
	    ArrayList<ExpenseBean> data = null;
	   
	    public CostsListAdapter(Context context, int layoutResourceId, ArrayList<ExpenseBean> data) {
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
	            holder.txtTitle = (TextView)row.findViewById(R.id.costs_list_item_title);
	            holder.txtAmount = (TextView)row.findViewById(R.id.costs_list_item_amount);
	            holder.txtDate = (TextView)row.findViewById(R.id.costs_list_item_date);
	            holder.txtDescription = (TextView)row.findViewById(R.id.costs_list_item_description);
	           
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (ItemHolder)row.getTag();
	        }
	       
	        ExpenseBean trb = data.get(position);

	        if ( trb != null ) {
	        	holder.txtTitle.setText( trb.getType() );
	        	DecimalFormat df = new DecimalFormat("#.00");
	        	holder.txtAmount.setText( "€"+df.format(trb.getAmount()) );
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	        	holder.txtDate.setText( sdf.format(trb.getDate().getTime()) );
	        	holder.txtDescription.setText( trb.getProject() + " ( " + trb.getClient() + " )" );
	        }
	        return row;
	    }
	   
	    static class ItemHolder
	    {
	        TextView txtTitle;
	        TextView txtAmount;
	        TextView txtDate;
	        TextView txtDescription;
	    }
	
}
