package com.maple.communication.nms;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import com.maple.communication.nms.Data.cmtsViewData;
import com.maple.communication.nms.DataManager.RAMDataStorage;
import com.maple.communication.nms.DataManager.RAMManager;


import java.util.ArrayList;

public class CmtsFragment extends ActionBarActivity implements CmtsAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private CmtsAdapter cmtsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

RAMManager ramManager;
    // private LinearLayoutManager mLayoutManager;
    public static final String SITE_TITLE = "SiteTitle";
    public static final String SITE_ID = "SiteID";
    String siteTitle = "";
    long siteID = 0;

    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmts_fragment);
        cmtsDatas = null;
        ramManager=RAMManager.getInstance(this);
        siteTitle = getIntent().getStringExtra(SITE_TITLE);
        siteID = getIntent().getLongExtra(SITE_ID, 0);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");


        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);


        toolbarTitle.setText(siteTitle);

        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CmtsFragment.this, SiteActivity.class);
                startActivity(intent);
                finish();
            }
        });


        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.cmts_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        showCMTSList(siteID);

        //  mAdapter.SetOnItemClickListener(() this) ;
        //

    }

    public static ArrayList<cmtsViewData> cmtsDatas;

    void showCMTSList(long siteID) {
        cmtsDatas = new ArrayList<>();
        cmtsDatas = RAMDataStorage.getInstance().getRamCMTSList(siteTitle);
            /*SQLiteDataManager sqLiteDataManager = new SQLiteDataManager(this);
            sqLiteDataManager.open();

            Cursor cmtsCursor = sqLiteDataManager.getCmtsTableOfSite(siteID);
            if (cmtsCursor != null && cmtsCursor.getCount() > 0) {
                cmtsCursor.moveToFirst();

                for (int i = 0; i < cmtsCursor.getCount(); i++) {

                    long cmtsID = cmtsCursor.getInt(cmtsCursor.getColumnIndex(SQLiteDataManager.ID));
                    String name = cmtsCursor.getString(cmtsCursor.getColumnIndex(SQLiteDataManager.NAME));
                    String ip = cmtsCursor.getString(cmtsCursor.getColumnIndex(SQLiteDataManager.IP));
                    String readCommunity = cmtsCursor.getString(cmtsCursor.getColumnIndex(SQLiteDataManager.READ_COMMUNITY));
                    String writeCommunity = cmtsCursor.getString(cmtsCursor.getColumnIndex(SQLiteDataManager.WRITE_COMMUNITY));

                    ArrayList<TransponderData> transponderDatas = new ArrayList<>();
                    CMTSData cmtsData = new CMTSData(ip, readCommunity, writeCommunity, name, cmtsID, transponderDatas);
                    cmtsDatas.add(cmtsData);
                    if (!cmtsCursor.isLast())
                        cmtsCursor.moveToNext();
                }
            }

            sqLiteDataManager.close();*/


    /* if(siteActivity.siteList!=null)
     {
         for(int i=0;i<siteActivity.siteList.size();i++)
         {
             if(siteActivity.siteList.get(i)._id==siteID)
             {
                 siteActivity.siteList.get(i).cmtsDatas=cmtsDatas;
                 break;
             }
         }
     }*/
    cmtsAdapter = new CmtsAdapter(cmtsDatas, this);
cmtsAdapter.mDataset=cmtsDatas;
    mRecyclerView.setAdapter(cmtsAdapter);
}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==R.id.add)
        {
            showCMTSAddScreen();
        }



        return super.onOptionsItemSelected(item);
    }

    void showCMTSAddScreen()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.cmts_add_layout);

        final EditText edtName = (EditText) dialog.findViewById(R.id.edtName);
        final EditText edtIP=(EditText)dialog.findViewById(R.id.edtIP);
        final EditText edtReadCommunity=(EditText)dialog.findViewById(R.id.edtReadCommunity);
        final EditText edtWriteCommunity=(EditText)dialog.findViewById(R.id.edtWriteCommunity);

        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        Button btncancel=(Button)dialog.findViewById(R.id.btncancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString();
                String ip = edtIP.getText().toString();
                String readCommunity = edtReadCommunity.getText().toString();
                String writeCommunity = edtWriteCommunity.getText().toString();
                String status = "";
                String macAddress = "";
                ramManager.insertCmtsTable(name,ip,readCommunity,writeCommunity,siteID,macAddress,status);
                progressBar.setVisibility(View.VISIBLE);

                Log.e("SNMP","Going to fetch data");
                RAMManager.getInstance(CmtsFragment.this).getDBDataToRAM();
                new LoadCMTS().execute("");
              //  showCMTSList(siteID);
               /* SQLiteDataManager sqLiteDataManager = new SQLiteDataManager(CmtsFragment.this);
                sqLiteDataManager.open();

                long cmtsID = sqLiteDataManager.insertCmtsTable(name, ip, readCommunity, writeCommunity, siteID, macAddress, status);


                if (cmtsID > 0) {
                    ArrayList<TransponderData> transponderDatas = new ArrayList<>();
                    CMTSData cmtsData = new CMTSData(ip, readCommunity, writeCommunity, name, cmtsID, transponderDatas);
                    cmtsDatas.add(cmtsData);
                }
               // sqLiteDataManager.checkAndInsertSiteTable(name);
                sqLiteDataManager.close();
                showCMTSList(siteID);*/
                dialog.dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // SearchManager searchManager =
        //         (SearchManager) getSystemService(context.SEARCH_SERVICE);
        // SearchView searchView =
        //       (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // searchView.setSearchableInfo(
        //        searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent=new Intent(CmtsFragment.this,TransponderFragment.class);
        intent.putExtra(CmtsFragment.SITE_TITLE,siteTitle);
        intent.putExtra(TransponderFragment.CMTS_TITLE,cmtsDatas.get(position).cmtsName);
        intent.putExtra(TransponderFragment.CMTS_IP,cmtsDatas.get(position).ipAddress);
        startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int position) {
        showDeleteMenu(menu);
    }

    class LoadCMTS extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {

            Log.e("SNMP","started the background thread to check for completion");
            while (!RAMManager.getInstance(CmtsFragment.this).isCompletedLoading)
            {
                try {
                    Thread.sleep(500);
                }
                catch (Exception e){e.printStackTrace();}
            }
            Log.e("SNMP","completed the background thread to check for completion");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            Log.e("SNMP","Dismissed the progressbar");
           showCMTSList(siteID);
            // mAdapter.Se
            // mAdapter.

            super.onPostExecute(s);
        }
    }

    void showDeleteMenu(final Menu menu)
    {
        MenuItem btnAdd = menu.findItem(R.id.add);
        // set your desired icon here based on a flag if you like
        btnAdd.setIcon(getResources().getDrawable(R.drawable.delete_icon));

        btnAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                for(int i=0;i<cmtsDatas.size();i++)
                {
                    if(cmtsDatas.get(i).isSelected)
                    {
                        long _id=cmtsDatas.get(i)._Id;

                        ramManager.DeleteCMTS(cmtsDatas.get(i)._Id);
                        cmtsDatas.remove(i);
                    }
                    // siteListItems.get(i).isSelected=false;
                }

                cmtsAdapter.notifyDataSetChanged();
                showAddMenu(menu);
               // progressBar.setVisibility(View.VISIBLE);
                //new LoadCMTS().execute("");
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
                showCMTSAddScreen();
                return true;
            }
        });
    }


    // mAdapter.
        //



}