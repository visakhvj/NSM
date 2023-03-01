package com.maple.communication.nms;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maple.communication.nms.Adapter.CmtsAdapter;
import com.maple.communication.nms.Adapter.TrasnponderAdapter;
import com.maple.communication.nms.Data.transponderViewData;
import com.maple.communication.nms.DataManager.RAMDataStorage;
import com.maple.communication.nms.DataManager.RAMManager;
import com.maple.communication.nms.SnmpManager.GlobalVars;


import java.util.ArrayList;

/**
 * Created by 1013373 on 02-05-2016.
 */
public class TransponderFragment extends AppCompatActivity implements TrasnponderAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private TrasnponderAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String CMTS_TITLE="CmtsTitle";
    public static final String CMTS_IP="CmtsIP";


    ProgressBar progressBar;

    // private LinearLayoutManager mLayoutManager;
    String siteTitle;
    String cmtsIP="";


    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transponder_layout);

        siteTitle=getIntent().getStringExtra(CmtsFragment.SITE_TITLE);
        cmtsIP=getIntent().getStringExtra(TransponderFragment.CMTS_IP);
        String cmtsTitle=getIntent().getStringExtra(TransponderFragment.CMTS_TITLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

       // toolbar.setSubtitle(cmtsTitle);
      //  toolbar.setTitleTextColor(Color.parseColor("#10c3eb"));
      //  toolbar.setSubtitleTextColor(Color.parseColor("#10c3eb"));

        setSupportActionBar(toolbar);

        TextView toolbarTitle= (TextView)toolbar.findViewById(R.id.toolbarTitle);
        TextView toolbarSubTitle= (TextView)toolbar.findViewById(R.id.toolbarSubTitle);

        toolbarTitle.setText(siteTitle);
        toolbarSubTitle.setText(cmtsTitle);

        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TransponderFragment.this,SiteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        toolbarSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransponderFragment.this, CmtsFragment.class);
                intent.putExtra(CmtsFragment.SITE_TITLE,siteTitle);

                startActivity(intent);
                finish();
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.cmts_recycler_view);
        progressBar=(ProgressBar)findViewById(R.id.  progressBar);
        progressBar.setVisibility(View.VISIBLE);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

       // DM,DSM3,SMG,UNKNOWN;
        String[] siteList = {GlobalVars.transpoderType.DM.toString(), GlobalVars.transpoderType.DSM3.toString(),GlobalVars.transpoderType.SMG.toString(),GlobalVars.transpoderType.UNKNOWN.toString()};
        // specify an adapter (see also next example)
        mAdapter = new TrasnponderAdapter(siteList,this,this);

        mAdapter.transDetails.put(GlobalVars.transpoderType.DM.toString(),null);
        mAdapter.transDetails.put(GlobalVars.transpoderType.DSM3.toString(),null);
        mAdapter.transDetails.put(GlobalVars.transpoderType.SMG.toString(),null);
        mAdapter.transDetails.put(GlobalVars.transpoderType.UNKNOWN.toString(),null);


        // mAdapter.Se
        // mAdapter.
        if (mAdapter ==null){
        }
        mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.SetOnItemClickListener(() this) ;
        //


        getTransponderDetails();
    }


    void getTransponderList()
    {
       // CMTSData cmtsDat = new CMTSData();

       /* CMTSData cmtsData=null;
       if(CmtsFragment.cmtsDatas!=null)
       {
           for(int i=0;i<CmtsFragment.cmtsDatas.size();i++)
           {
               if(CmtsFragment.cmtsDatas.get(i).==cmtsID)
               {
                   cmtsData=CmtsFragment.cmtsDatas.get(i);
               }
           }
       }


        snmpwrap = new SNMPWrapper(this);
        ArrayList<TransponderData> transListupdatet = new ArrayList<TransponderData>();

String ip=cmtsData.ipAddress;
       // ip="10.1.20.3";
        snmpwrap.getTransponderList(ip, CMTS_PORT, PUBLIC_COMMUNITY);*/


       // snmpwrap.getTransponderList("10.1.20.3", CMTS_PORT, PUBLIC_COMMUNITY);
    }


    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trans, menu);
        // SearchManager searchManager =
        //         (SearchManager) getSystemService(context.SEARCH_SERVICE);
        // SearchView searchView =
        //       (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // searchView.setSearchableInfo(
        //        searchManager.getSearchableInfo(getComponentName()));

        this.menu=menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.add) {
            showConfigureMenu(menu);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {



    }


    public void getTransponderDetails() {
        ArrayList<transponderViewData> transDetails = RAMDataStorage.getInstance().getRamTransList(siteTitle,cmtsIP);

        mAdapter.transDataList=transDetails;

        final ArrayList<transponderViewData> dm=new ArrayList<>();
        final ArrayList<transponderViewData> dsm3=new ArrayList<>();
        final ArrayList<transponderViewData> smg=new ArrayList<>();
        final ArrayList<transponderViewData> unknown=new ArrayList<>();

        for(int i=0;i<transDetails.size();i++)
        {
            try {
                if (transDetails.get(i).transType.equals(GlobalVars.transpoderType.DM)) {
                    dm.add(transDetails.get(i));
                } else if (transDetails.get(i).transType.equals(GlobalVars.transpoderType.DSM3)) {
                    dsm3.add(transDetails.get(i));
                } else if (transDetails.get(i).transType.equals(GlobalVars.transpoderType.SMG)) {
                    smg.add(transDetails.get(i));
                } else {
                    unknown.add(transDetails.get(i));
                }
            }
            catch (Exception e){e.printStackTrace();
                unknown.add(transDetails.get(i));}
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.transDetails.put(GlobalVars.transpoderType.DM.toString(),dm);
                mAdapter.transDetails.put(GlobalVars.transpoderType.DSM3.toString(),dsm3);
                mAdapter.transDetails.put(GlobalVars.transpoderType.SMG.toString(),smg);
                mAdapter.transDetails.put(GlobalVars.transpoderType.UNKNOWN.toString(),unknown);
                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }
        });

    }


   /* public void updateTransList(CMTSData cmtsData) {

        SQLiteDataManager sqLiteDataManager=new SQLiteDataManager(this);
        sqLiteDataManager.open();
        if(cmtsData!=null) {
            for (int i = 0; i < cmtsData.transDataList.size(); i++) {
                String community=null;
                Cursor transponderDetails=sqLiteDataManager.getTransponderTable(cmtsData.transDataList.get(i).ipAddress);
                if(transponderDetails!=null&&transponderDetails.getCount()>0)
                {
                    transponderDetails.moveToFirst();
                    community=transponderDetails.getString(transponderDetails.getColumnIndex(SQLiteDataManager.READ_COMMUNITY));
                }
                transponderDetails.close();
                cmtsData.transDataList.get(i).community=community;
                Log.i("Test", "Got ipaddress and macID :" + cmtsData.transDataList.get(i).ipAddress + "MAC : " + cmtsData.transDataList.get(i).macID);
            }
            snmpwrap.getTransponderDetails(cmtsData);
        }
        else
        {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
        sqLiteDataManager.close();
    }*/


    // mAdapter.
    //


    @Override
    public boolean onItemLongClicked(int type, long position) {
        showConfigureMenu(menu);
        return  true;
    }



    void showConfigureMenu(final Menu menu)
    {
        MenuItem btnAdd = menu.findItem(R.id.add);
        // set your desired icon here based on a flag if you like
        btnAdd.setIcon(getResources().getDrawable(R.drawable.ic_settings_black_24dp));

        btnAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                final ArrayList<transponderViewData> selectedList=new ArrayList<transponderViewData>();
                for(int i=0;i<mAdapter.transDetails.size();i++) {

                    final String name = mAdapter.mDataset[i];
                    final ArrayList<transponderViewData> transponderDatas = mAdapter.transDetails.get(name);
                  for(int j=0;j<transponderDatas.size();j++) {
                      if (transponderDatas.get(j).isSelected)
                      {
                          selectedList.add(transponderDatas.get(j));
                      }
                  }

                    showConfigure(selectedList);

                    // progressBar.setVisibility(View.VISIBLE);
                    //new LoadCMTS().execute("");
                }
                return true;
            }
        });
    }


    void showAddMenu(Menu menu)
    {
        MenuItem btnAdd = menu.findItem(R.id.add);
        // set your desired icon here based on a flag if you like
        btnAdd.setIcon(getResources().getDrawable(R.drawable.addbtn));
        btnAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               // showCMTSAddScreen();
                return true;
            }
        });
    }

    Dialog dialog;
    void showConfigure(final ArrayList<transponderViewData> selectedList)
    {
        if(dialog!=null)
            dialog.dismiss();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.transponder_configure);


        final EditText edtReadCommunity=(EditText)dialog.findViewById(R.id.edtReadCommunity);
        final EditText edtWriteCommunity=(EditText)dialog.findViewById(R.id.edtWriteCommunity);

        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        Button btncancel=(Button)dialog.findViewById(R.id.btncancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String readCommunity = edtReadCommunity.getText().toString();
                String writeCommunity = edtWriteCommunity.getText().toString();

                for (int i = 0; i < selectedList.size(); i++) {
                    String status="";
                    if(selectedList.get(i).status!=null)
                        status=selectedList.get(i).status.toString();
                    RAMManager.getInstance(TransponderFragment.this).insertTransponderTable(selectedList.get(i).ipAddress, readCommunity, writeCommunity, selectedList.get(i).cmtsID, selectedList.get(i).MAC, status);
                }
              //  progressBar.setVisibility(View.VISIBLE);

                dialog.dismiss();

                showAddMenu(menu);
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showAddMenu(menu);
            }
        });

        dialog.show();
    }





}
