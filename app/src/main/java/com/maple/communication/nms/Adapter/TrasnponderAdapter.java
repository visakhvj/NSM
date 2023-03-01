package com.maple.communication.nms.Adapter;


import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maple.communication.nms.Data.transponderViewData;
import com.maple.communication.nms.R;
import com.maple.communication.nms.SnmpManager.GlobalVars;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.OnClickListener;

/**
 * Created by Vineesh C on 30-01-2016.
 */
public class TrasnponderAdapter extends RecyclerView.Adapter<TrasnponderAdapter.ViewHolder> {
    public String[] mDataset;

    public ArrayList<transponderViewData> transDataList;
    public HashMap<String,ArrayList<transponderViewData>> transDetails;
    int pos;
    public static OnItemClickListener mItemClickListener;


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public boolean onItemLongClicked(int type,long position);

    }




    public  class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public TextView siteName;
        public TextView sitePlace;
        public TextView siteId;
        public ImageView siteImage;
       // public ImageView siteAlarm;
        public LinearLayout lnrTransponderView;
        public LinearLayout lnrRowParent;

        @Override
        public void onClick(View v) {

            LinearLayout lnrTransponderView=(LinearLayout)v.findViewById(R.id.lnrTransponderView);

            if(lnrTransponderView.getVisibility()==View.GONE)
                lnrTransponderView.setVisibility(View.VISIBLE);
            else
            lnrTransponderView.setVisibility(View.GONE);
            // Log.e(">>>>", "??>>>>>>>>Hlsf done");
            if (mItemClickListener == null){
                // Log.e(">>>>", "??>>>>>>>>mItemClickListener is NULLLLLLL ");
            }else {
                // Log.e(">>>>", "??>>>>>>>>mItemClickListener is NOT NULLLLLLL ");
                mItemClickListener.onItemClick(v, getAdapterPosition()); //OnItemClickListener mItemClickListener;
            }
        }



        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            siteName = (TextView)v.findViewById(R.id.title);
            sitePlace = (TextView)v.findViewById(R.id.cmtscount );
            siteId = (TextView)v.findViewById(R.id.duration);
            siteImage = (ImageView)v.findViewById(R.id.list_image);
            //siteAlarm = (ImageView)v.findViewById(R.id.alarmimage);
            lnrTransponderView=(LinearLayout)v.findViewById(R.id.lnrTransponderView);
            lnrRowParent=(LinearLayout)v.findViewById(R.id.lnrRowParent);
            lnrTransponderView.setVisibility(View.GONE);
            v.setOnClickListener(this);




        }

    }
    @Override
    public TrasnponderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transpoder_row, parent, false);
        // set the view's size, margins, paddings and layout parameterssit

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    Activity activity;
    public TrasnponderAdapter(String[] myDataset, Activity activity,OnItemClickListener onItemClickListener) {
        //  public CmtsAdapter(String[] myDataset,OnItemClickListener onitmlst) {
        transDataList=new ArrayList<>();
        transDetails=new HashMap<>();
        mDataset = myDataset;
        this.activity=activity;
        mItemClickListener=onItemClickListener;
        // mItemClickListener = onitmlst;
    }

    @Override
    public void onBindViewHolder(TrasnponderAdapter.ViewHolder holder, final int position) {


        holder.lnrTransponderView.removeAllViews();
        final String name = mDataset[position];
       final ArrayList<transponderViewData> transponderDatas=transDetails.get(name);
        holder.siteName.setText(mDataset[position]);
        holder.siteImage.setImageResource(R.drawable.transpondertop);
        pos= position;

        if(transponderDatas!=null) {
            for (int i = 0; i < transponderDatas.size(); i++) {
                final int index=i;
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View custView = layoutInflater.inflate(R.layout.unit_row, null, false);

                final LinearLayout lnrRowParent = (LinearLayout) custView.findViewById(R.id.lnrRowParent);
                final ImageView alarmimage = (ImageView) custView.findViewById(R.id.alarmimage);

                final LinearLayout lnrfrmware = (LinearLayout) custView.findViewById(R.id.lnrfrmware);
                final LinearLayout lnralarams = (LinearLayout) custView.findViewById(R.id.lnralarams);
                final LinearLayout lnrSystemUpTime = (LinearLayout) custView.findViewById(R.id.lnrSystemUpTime);

                TextView transcount=(TextView)custView.findViewById(R.id.transcount);
                TextView majoralrmcount=(TextView)custView.findViewById(R.id.majoralrmcount);
                TextView minoralrmcount=(TextView)custView.findViewById(R.id.minoralrmcount);
                TextView duration=(TextView)custView.findViewById(R.id.duration);
                TextView systemuptime=(TextView)custView.findViewById(R.id.systemuptime);
                TextView majoralrm=(TextView)custView.findViewById(R.id.majoralrm);
                TextView minoralarm=(TextView)custView.findViewById(R.id.minoralarm);



                if(transponderDatas.get(i).MAC!=null)
                 transcount.setText("MacID:"+transponderDatas.get(i).MAC);
                if(transponderDatas.get(i).ipAddress!=null)
                    majoralrmcount.setText("IP Address:"+transponderDatas.get(i).ipAddress);
                if(transponderDatas.get(i).FWRev!=null)
                    minoralrmcount.setText("Firmware version:"+transponderDatas.get(i).FWRev);
                else
                    minoralrmcount.setText("");
                if(transponderDatas.get(i).HwRev!=null)
                    duration.setText("Hardware Revision:"+transponderDatas.get(i).HwRev);
                else
                    duration.setText("");
                if(transponderDatas.get(i).sysupTime!=null)
                    systemuptime.setText("System Up Time:"+transponderDatas.get(i).sysupTime);
                else
                    systemuptime.setText("");

                majoralrm.setText("Major Alarms : "+transponderDatas.get(i).majorAlarmCount);
                minoralarm.setText("Minor Alarms : "+transponderDatas.get(i).minorrAlarmCount);
                majoralrm.setVisibility(View.GONE);
                minoralarm.setVisibility(View.GONE);

                if(transponderDatas.get(i).alarm!=null)
                {
                    if(transponderDatas.get(i).alarm.equals(GlobalVars.transpoderAlarm.MAJOR))
                    {
                        alarmimage.setImageResource(R.drawable.red);
                    }
                    else if(transponderDatas.get(i).alarm.equals(GlobalVars.transpoderAlarm.MINOR))
                    {
                        alarmimage.setImageResource(R.drawable.yellow);
                    }
                    else
                    {
                       // alarmimage.setImageResource(null);
                    }
                }



                alarmimage.setVisibility(View.GONE);
                lnrfrmware.setVisibility(View.GONE);
                lnralarams.setVisibility(View.GONE);
                lnrSystemUpTime.setVisibility(View.GONE);


                holder.lnrTransponderView.addView(custView);

                custView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(transponderDatas.get(index).transType!=null) {

                            if (lnralarams.getVisibility() == View.GONE) {
                                lnrfrmware.setVisibility(View.VISIBLE);
                                lnralarams.setVisibility(View.GONE);
                                lnrSystemUpTime.setVisibility(View.VISIBLE);
                                alarmimage.setVisibility(View.VISIBLE);

                                lnrRowParent.setBackgroundResource(R.color.list_sub_1);
                            } else {
                                lnrfrmware.setVisibility(View.GONE);
                                lnralarams.setVisibility(View.GONE);
                                lnrSystemUpTime.setVisibility(View.GONE);
                                alarmimage.setVisibility(View.GONE);

                                lnrRowParent.setBackgroundResource(R.color.list_sub_2);
                            }
                        }
                    }
                });

                custView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (v.isSelected()) {
                            lnrRowParent.setBackgroundColor(Color.parseColor("#CBE4E4"));

                            v.setSelected(false);
                            transponderDatas.get(index).isSelected=false;

                        } else {
                            if(mItemClickListener!=null)
                                mItemClickListener.onItemLongClicked(position,index);//OnItemClickListener mItemClickListener;
                            lnrRowParent.setBackgroundColor(Color.parseColor("#87ecf9"));

                            transponderDatas.get(index).isSelected=true;
                            v.setSelected(true);
                        }

                        return true;
                    }
                });


            }
        }


     /*   holder.siteName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("", ">>>>>CLickd on :" + pos);
                //  remove(name);
            }
        });*/

        //  holder.siteName.setText(" Sample Text");
        /*holder.siteId.setText("10 Minutes Ago");
        if (position ==2){
            holder.siteAlarm.setBackgroundResource(R.drawable.red);
        }
        else if (position ==4) {
            holder.siteAlarm.setBackgroundResource(R.drawable.yellow);
        }*/

    }

    @Override
    public int getItemCount() {
        return mDataset.length;    }
}

