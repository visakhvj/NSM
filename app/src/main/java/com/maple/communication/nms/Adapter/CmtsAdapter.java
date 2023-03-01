package com.maple.communication.nms.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maple.communication.nms.Data.cmtsViewData;
import com.maple.communication.nms.R;


import java.util.ArrayList;


/**
 * Created by Vineesh C on 30-01-2016.
 */
public class CmtsAdapter extends Adapter<CmtsAdapter.ViewHolder> {
    public ArrayList<cmtsViewData> mDataset;
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
        public TextView sitePlace;
        public ImageView siteImage;
        public ImageView siteAlarm;
        public TextView transcount;
        public TextView majoralrmcount;
        public TextView minoralrmcount;
        public TextView duration;
        public LinearLayout thumbnail;
        public LinearLayout lnrList;
        public View view;

        @Override
        public void onClick(View v) {
           // Log.e(">>>>", "??>>>>>>>>Hlsf done");
            if (mItemClickListener == null){
               // Log.e(">>>>", "??>>>>>>>>mItemClickListener is NULLLLLLL ");
            }else {
               // Log.e(">>>>", "??>>>>>>>>mItemClickListener is NOT NULLLLLLL ");
                mItemClickListener.onItemClick(v, getAdapterPosition()); //OnItemClickListener mItemClickListener;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.isSelected()) {
                v.setBackgroundColor(Color.WHITE);
                lnrList.setBackgroundColor(Color.WHITE);
                thumbnail.setBackgroundColor(Color.WHITE);
                v.setSelected(false);
                mDataset.get(getAdapterPosition()).isSelected = true;

            } else {
                mItemClickListener.onItemLongClick(v, getAdapterPosition()); //OnItemClickListener mItemClickListener;
                v.setBackgroundColor(Color.parseColor("#87ecf9"));
                lnrList.setBackgroundColor(Color.parseColor("#87ecf9"));
                thumbnail.setBackgroundColor(Color.parseColor("#87ecf9"));
                mDataset.get(getAdapterPosition()).isSelected = true;
                v.setSelected(true);
            }
            return true;

        }


        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            siteName = (TextView)v.findViewById(R.id.title);
            sitePlace = (TextView)v.findViewById(R.id.cmtscount );
            duration = (TextView)v.findViewById(R.id.duration);
            siteImage = (ImageView)v.findViewById(R.id.list_image);
            siteAlarm = (ImageView)v.findViewById(R.id.alarmimage);
            transcount=(TextView)v.findViewById(R.id.transcount);
            majoralrmcount=(TextView)v.findViewById(R.id.majoralrmcount);
            minoralrmcount=(TextView)v.findViewById(R.id.minoralrmcount);
            thumbnail=(LinearLayout)v.findViewById(R.id.thumbnail);
            lnrList=(LinearLayout)v.findViewById(R.id.lnrList);
            this.view=v;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

    }
    @Override
    public CmtsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cmtsrow, parent, false);
        // set the view's size, margins, paddings and layout parameterssit

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    public CmtsAdapter(ArrayList<cmtsViewData> myDataset, OnItemClickListener onItemClickListener) {
      //  public CmtsAdapter(String[] myDataset,OnItemClickListener onitmlst) {
        mDataset = myDataset;
        mItemClickListener=onItemClickListener;
       // mItemClickListener = onitmlst;
    }

    @Override
    public void onBindViewHolder(CmtsAdapter.ViewHolder holder, int position) {
        final String name = mDataset.get(position).cmtsName;
        holder.siteName.setText(name);
        pos= position;
        holder.sitePlace.setText("IP : "+mDataset.get(position).ipAddress);
        holder.transcount.setText("DM:"+mDataset.get(position).DMCount+" , DSM3:"+mDataset.get(position).DSM3Count+" , SMG:"+mDataset.get(position).SMGCount);
        holder.majoralrmcount.setText("Major Alarm : "+mDataset.get(position).majorAlarmCount);
        holder.minoralrmcount.setText("Minor Alarm : "+mDataset.get(position).minorrAlarmCount);


     /*   holder.siteName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("", ">>>>>CLickd on :" + pos);
                //  remove(name);
            }
        });*/

      //  holder.siteName.setText(" Sample Text");

        if (mDataset.get(position).isSelected) {

            holder.view.setBackgroundColor(Color.parseColor("#87ecf9"));
          //  holder.lnrList.setBackgroundColor(Color.parseColor("#87ecf9"));
         //   holder.thumbnail.setBackgroundColor(Color.parseColor("#87ecf9"));
            holder.view.setSelected(true);
        } else {

          //  holder.view.setBackgroundColor(Color.parseColor("#E0F7FA"));
          //  holder.lnrList.setBackgroundColor(Color.parseColor("#EBF6FC"));
          //  holder.thumbnail.setBackgroundColor(Color.parseColor("#7092BE"));
            holder.view.setSelected(false);
        }
        holder.duration.setText("");
        if (position ==2){
            holder.siteAlarm.setBackgroundResource(R.drawable.red);
        }
        else if (position ==4) {
            holder.siteAlarm.setBackgroundResource(R.drawable.yellow);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();    }
}
