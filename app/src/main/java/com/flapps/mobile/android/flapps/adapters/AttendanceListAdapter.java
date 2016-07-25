package com.flapps.mobile.android.flapps.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.flapps.mobile.android.flapps.ComeInOutActivity;
import com.flapps.mobile.android.flapps.R;
import com.flapps.mobile.android.flapps.data.AttendanceBean;
import com.flapps.mobile.android.flapps.data.AttendanceHashMap;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.RecyclerViewHolders> {

   private List<AttendanceBean> itemList;
   private Context context;
	private ComeInOutActivity handler;

   public AttendanceListAdapter(Context context, AttendanceHashMap itemList, ComeInOutActivity handler ) {
       this.itemList = new ArrayList<AttendanceBean>();
	   Iterator<Long> i = itemList.keySet().iterator();
	   while( i.hasNext() ) {
		   AttendanceBean ab = itemList.get( i.next() );
		   if ( ab.getId() == null ) continue;
		   if ( ab.getId() == -1 ) continue;
		   this.itemList.add( ab );
	   }
	   Collections.sort( this.itemList );
       this.context = context;
	   this.handler = handler;

	   /*
	   Long key = -2l;
	   Iterator<Long> i = itemList.keySet().iterator();
	   while( i.hasNext() ) {
		   key = i.next();
		   if ( "work".equals( itemList.get( key ).getTitle().toLowerCase() ) ) {
			   //this.itemList.remove( key );
			   break;
		   }
	   }
	   this.itemList.remove( key );
	   */
	   //this.itemList.remove( new Long(-1) );
	   //this.itemList.remove( null );
   }

   @Override
   public AttendanceListAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

       View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_list_item, null);
       AttendanceListAdapter.RecyclerViewHolders rcv = new AttendanceListAdapter.RecyclerViewHolders(layoutView);
       return rcv;
   }

   @Override
   public void onBindViewHolder(AttendanceListAdapter.RecyclerViewHolders holder, int position) {

	   /*
	   if ( position == 0  ) {
	       holder.firstRow.setText( "Clock" );
	       holder.secondRow.setText( "IN" );
		   holder.id = -1l;
	   } else if ( position == 1 ) {
	       holder.firstRow.setText( "Clock" );
	       holder.secondRow.setText( "OUT" );
		   holder.id = 0l;
	   } else {
	   */

	        if ( position == (itemList.size() -1 ) ) {
		        holder.firstRow.setVisibility(View.GONE);
		        holder.secondRow.setText("END");
	        } else {
		        if (itemList.get(position).isActive()) holder.firstRow.setText("Start");
		        else holder.firstRow.setText("Start");
		        holder.secondRow.setText(itemList.get(position).getTitle().toUpperCase());
	        }
		   holder.id = itemList.get(position).getId();
	   /*
	   }
	   */
   }

   @Override
   public int getItemCount() {
       return this.itemList.size();
   }
   
   public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

	    public TextView firstRow;
	    public TextView secondRow;
	    public Long id;

	    public RecyclerViewHolders(View itemView) {
	        super(itemView);
	        itemView.setOnClickListener(this);
	        firstRow = (TextView)itemView.findViewById(R.id.textlabel_row_1);
	        secondRow = (TextView)itemView.findViewById(R.id.textlabel_row_2);
	    }

	    @Override
	    public void onClick(View view) {

		    /*
			if ( id == -1l ) {
				handler.clockInButtonAction(view);
			}
			else  if ( id ==0 ) {
				handler.clockOutButtonAction( view );
			}
			else {
			*/
				handler.onClick( id );
			/*
	        }
	        */
	    }
	}
}