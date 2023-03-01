package com.maple.communication.nms.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maple.communication.nms.Data.siteViewData;
import com.maple.communication.nms.R;
import com.maple.communication.nms.SiteActivity;
import com.maple.communication.nms.TreeActivity;

import java.util.ArrayList;

/**
 * Created by Vineesh C on 30-01-2016.
 */
public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ViewHolder>  {
    public ArrayList<siteViewData> mDataset;
     int pos;
    public static OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);

    }

  //  public  void SetOnItemClickListener(final OnItemClickListener aItemClickListener) {
   //     this.mItemClickListener = aItemClickListener;

   // }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        public TextView siteName;
        public TextView cmtscount;
        public TextView siteId;
        public ImageView siteImage;
        public ImageView siteAlarm;
        public View view;
        public TextView transcount;
        public TextView majoralrmcount;
        public TextView minoralrmcount;
       public FloatingActionButton fab;

        @Override
        public void onClick(View v) {
           // Log.e(">>>>", "??>>>>>>>>Hlsf done");
            if (mItemClickListener == null){
               // Log.e(">>>>", "??>>>>>>>>mItemClickListener is NULLLLLLL ");
            }else {
               // Log.e(">>>>", "??>>>>>>>>mItemClickListener is NOT NULLLLLLL ");

                if(!v.isSelected()) {
                    mItemClickListener.onItemClick(v, getAdapterPosition()); //OnItemClickListener mItemClickListener;
                }
                else
                {
                    v.performLongClick();
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mItemClickListener == null){
                // Log.e(">>>>", "??>>>>>>>>mItemClickListener is NULLLLLLL ");
            }else {
                // Log.e(">>>>", "??>>>>>>>>mItemClickListener is NOT NULLLLLLL ");
                if(v.isSelected())
                {
                    v.setBackgroundColor(Color.parseColor("#E0F7FA"));
                    v.setSelected(false);
                    mDataset.get(getAdapterPosition()).isSelected=true;

                }
                else {
                    mItemClickListener.onItemLongClick(v, getAdapterPosition()); //OnItemClickListener mItemClickListener;
                    v.setBackgroundColor(Color.parseColor("#87ecf9"));
                    mDataset.get(getAdapterPosition()).isSelected=true;
                    v.setSelected(true);
                }

            }
            return true;
        }

        // each data item is just a string in this case
        public ViewHolder(View v, final Context c) {
            super(v);
            siteName = (TextView)v.findViewById(R.id.title);
            cmtscount = (TextView)v.findViewById(R.id.cmtscount );
            siteId = (TextView)v.findViewById(R.id.duration);
            transcount=(TextView)v. findViewById(R.id.transcount);
            siteImage = (ImageView)v.findViewById(R.id.list_image);
            siteAlarm = (ImageView)v.findViewById(R.id.alarmimage);
            majoralrmcount=(TextView)v.findViewById(R.id.majoralrmcount);
            minoralrmcount=(TextView)v.findViewById(R.id.minoralrmcount);
            fab=(FloatingActionButton)v.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(c,TreeActivity.class);
                    c.startActivity(intent);
                }
            });
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            this.view=v;
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listrow, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //View v =null;
        ViewHolder vh = new ViewHolder(v,parent.getContext());
       return vh;
    }
    public SiteAdapter(ArrayList<siteViewData> myDataset, OnItemClickListener onitmlst) {
        mDataset = myDataset;
        mItemClickListener = onitmlst;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String name = mDataset.get(position).siteName;
        holder.siteName.setText(name);
        pos= position;
        holder.cmtscount.setText("CMTS:"+mDataset.get(position).cmtsCount);
        holder.transcount.setText("DM:"+mDataset.get(position).DMCount+", "+ "DSM3:"+mDataset.get(position).DSM3Count+", SMG:"+mDataset.get(position).SMGCount);
        holder.majoralrmcount.setText("Major Alarm:"+mDataset.get(position).majorAlarmCount);
        holder.minoralrmcount.setText("Minor Alarm:"+mDataset.get(position).minorrAlarmCount);
        if(mDataset.get(position).isSelected)
        {
            holder.view.setSelected(true);
           // holder.view.setBackgroundColor(Color.parseColor("#87ecf9"));
        }
        else
        {
            holder.view.setSelected(false);
          //  holder.view.setBackgroundColor(Color.parseColor("#E0F7FA"));
        }
     /*   holder.siteName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("", ">>>>>CLickd on :" + pos);
                //  remove(name);
            }
        });*/

      //  holder.siteName.setText(" Sample Text");
        holder.siteId.setText("");
        if (position ==3){
        //    holder.siteAlarm.setBackgroundResource(R.drawable.red);
        }
        else if (position ==5) {
          //  holder.siteAlarm.setBackgroundResource(R.drawable.yellow);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();    }
}
