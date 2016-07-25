package com.flapps.mobile.android.flapps.adapters;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.flapps.mobile.android.flapps.R;
import com.flapps.mobile.android.flapps.adapters.CostsListAdapter.ItemHolder;
import com.flapps.mobile.android.flapps.data.ClockInOutBean;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.ExpenseBean;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClockInOutListAdapter extends ArrayAdapter<ClockInOutBean> {
	
	Context context;
    int layoutResourceId;   
    ArrayList<ClockInOutBean> data = null;

	public ClockInOutListAdapter(Context context, int layoutResourceId, ArrayList<ClockInOutBean> data) {
		super(context, layoutResourceId, data);
		
		this.context = context;
		this.layoutResourceId = layoutResourceId;
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
            holder.txtName = (TextView)row.findViewById(R.id.clockinout_name);
            holder.txtCard = (TextView)row.findViewById(R.id.clockinout_card);
            holder.txtDate = (TextView)row.findViewById(R.id.clockinout_date);
            holder.imgAction = (ImageView)row.findViewById(R.id.clockinout_icon);
           
            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }
       
		Collections.sort(data);
        ClockInOutBean ciob = data.get(position);

        if ( ciob != null ) {
        	if ( ciob.getName() == null || "".equals(ciob.getName()) ) {
        		DataModel dm = new DataModel(context);
        		try {
					ciob.setName(dm.readCardName(ciob.getCardId()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if ( ciob.getName() == null ) holder.txtName.setText( "N/A" );
        	else if ( "".equals( ciob.getName() ) ) holder.txtName.setText( "N/A" );
        	else holder.txtName.setText( ciob.getName() );
        	holder.txtCard.setText( ciob.getCardId() );
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd H:mm:ss");
        	holder.txtDate.setText( sdf.format( ciob.getTime() ) );
        	if ( ciob.getActivityId() == null ) {
        		holder.imgAction.setImageResource(R.drawable.clock_in);
        	}
        	else if ( ciob.getActivityId() != 0)
        		holder.imgAction.setImageResource(R.drawable.clock_in);
        	else holder.imgAction.setImageResource(R.drawable.clock_out);
        }
        return row;
    }
   
    static class ItemHolder
    {
        TextView txtName;
        TextView txtCard;
        TextView txtDate;
        ImageView imgAction;
    }

}
