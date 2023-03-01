package com.maple.communication.nms;

import android.app.Dialog;
import android.content.Intent;
//import android.support.design.widget.FloatingActionButton;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import com.maple.communication.nms.Adapter.SiteAdapter;
import com.maple.communication.nms.Data.siteViewData;
import com.maple.communication.nms.DataManager.RAMDataStorage;
import com.maple.communication.nms.DataManager.RAMManager;

import java.util.ArrayList;


public class SiteActivity extends ActionBarActivity  implements SiteAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private SiteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RAMManager RAMMgr;
    public static OnNetworkView onNetworkSwitchSelect;
    ProgressBar progressBar;

    ArrayList<siteViewData> siteDatas;


    public interface OnNetworkView {
        public void onNetworkSwitch(View view, int position);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_fragment);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        RAMMgr = RAMManager.getInstance(this);
        // Progress bar till snmp complete


        RAMDataStorage RamData = RAMDataStorage.getInstance();
        Log.e("","*********************** YO YO  STRATSINSG ********************");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        /*RAMMgr.AddSite("aaa");
        RAMMgr.AddSite("ccc");
        RAMMgr.AddSite("vvv");
        RAMMgr.AddSite("mmm");
        RAMMgr.AddSite("hhh");
        RAMMgr.AddSite("kkk");
        RAMMgr.DeleteSite("lll");*/
       // ArrayList<SiteListItem> stList= RAMMgr.getDBDataToRAM();
      //  long  id = stList.get(2)._id;
       // RAMMgr.insertCmtsTable("CMTS1", "10.1.20.3", "public", "private", id, "ABCBCBC", null);
       // RAMMgr.deleteCMTSfromTableForSite(4);

        //RAMMgr.getSiteListFromRAMData();
        //insertCmtsTable( "CMTS1","10.1.1.20","public","private",long siteID,String macID,String status)






       /* RAMMgr = new RAMManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //onNetworkSwitchSelect =(siteActivity.OnNetworkView) this;
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

       /* siteList.add(new SiteListItem("New York",false));
        siteList.add(new SiteListItem("Los Angeles",false));
        siteList.add(new SiteListItem("Hauston",false));
        siteList.add(new SiteListItem("Chicago",false));
        siteList.add(new SiteListItem("Washinton",false));
        siteList.add(new SiteListItem("Boston",false));
        siteList.add(new SiteListItem("Seattle",false));
        siteList.add(new SiteListItem("Okland",false));
        siteList.add(new SiteListItem("Austin",false));
        siteList.add(new SiteListItem("Phoenix",false));
        siteList.add(new SiteListItem("Albany",false));
        siteList.add(new SiteListItem("Denver",false));
        siteList.add(new SiteListItem("Provo",false));*/

       // showSiteList();
      //  ArrayList<siteData> siteArray = RAMMgr.getSiteListFromRAMData();
        // specify an adapter (see also next example)
        siteDatas=new ArrayList<siteViewData>();
        mAdapter = new SiteAdapter(siteDatas, this);
        // mAdapter.Se
        // mAdapter.
        mRecyclerView.setAdapter(mAdapter);

new LoadSite().execute("");


       /* FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SiteActivity.this,TreeActivity.class);
                startActivity(intent);
            }
        });*/
    }


    Menu menu;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        if (id == R.id.add) {
            showSiteAddScreen();
        }

        return super.onOptionsItemSelected(item);
    }



/*

    public void showSiteList() {
        if (siteList == null) {
         //   siteList = new ArrayList<>();
           // SQLiteDataManager sqLiteDataManager = new SQLiteDataManager(this);
           // sqLiteDataManager.open();


            Cursor siteListCursor = sqLiteDataManager.getSiteTable();
            if (siteListCursor != null && siteListCursor.getCount() > 0) {
                siteListCursor.moveToFirst();

                for (int i = 0; i < siteListCursor.getCount(); i++) {
                    int _id = siteListCursor.getInt(siteListCursor.getColumnIndex(SQLiteDataManager.ID));
                    String siteName = siteListCursor.getString(siteListCursor.getColumnIndex(SQLiteDataManager.SITE_NAME));
                    ArrayList<CMTSData> cmtsDatas = getCmtsData(_id);
                    SiteListItem siteListItem = new SiteListItem(siteName, false, _id, cmtsDatas);
                    siteList.add(siteListItem);
                    if (!siteListCursor.isLast())
                        siteListCursor.moveToNext();

                }
            }
            siteListCursor.close();
            sqLiteDataManager.close();
        }

        // specify an adapter (see also next example)
        mAdapter = new SiteAdapter(siteList, this);
        // mAdapter.Se
        // mAdapter.
        mRecyclerView.setAdapter(mAdapter);
    }
/*
    ArrayList<CMTSData> getCmtsData(long siteID) {
        ArrayList<CMTSData> cmtsDatas = new ArrayList<>();
        SQLiteDataManager sqLiteDataManager = new SQLiteDataManager(this);
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

        sqLiteDataManager.close();
        return cmtsDatas;
    }
*/
   public void showSiteAddScreen() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.site_add_layout);

        final EditText edtName = (EditText) dialog.findViewById(R.id.edtName);
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString();
              /*  SQLiteDataManager sqLiteDataManager = new SQLiteDataManager(siteActivity.this);
                sqLiteDataManager.open();
                long siteID = sqLiteDataManager.checkAndInsertSiteTable(name);
                ArrayList<CMTSData> cmtsDatas = new ArrayList<CMTSData>();
                if (siteID > 0) {
                    siteList.add(new SiteListItem(name, false, siteID, cmtsDatas));
                }
                sqLiteDataManager.close();
                showSiteList();*/
                RAMMgr.AddSite(name);
                refreshSiteList();
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


    class LoadSite extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            while (!RAMMgr.isCompletedLoading)
            {
                try {
                    Thread.sleep(500);
                }
                catch (Exception e){e.printStackTrace();}
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            loadSiteList();
            // mAdapter.Se
            // mAdapter.

            super.onPostExecute(s);
        }
    }

    public void refreshSiteList()
    {

        progressBar.setVisibility(View.VISIBLE);
        RAMMgr.getDBDataToRAM();
        new LoadSite().execute("");
    }

    void loadSiteList()
    {
        siteDatas=RAMDataStorage.getInstance().getSiteList();
if(siteDatas!=null) {
    mAdapter.mDataset = siteDatas;
    mAdapter.notifyDataSetChanged();
}
    }


    @Override
    public void onItemClick(View view, int position) {
        Log.e(">>>>>>>>>>>>>>>>","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
        //onNetworkSwitchSelect.onNetworkSwitch(view ,position);
        Intent intent = new Intent(this, CmtsFragment.class);
        intent.putExtra(CmtsFragment.SITE_TITLE,siteDatas.get(position).siteName);
        intent.putExtra(CmtsFragment.SITE_ID,siteDatas.get(position)._id);
        intent.putExtra("siteno",position);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        MenuAdd menuAdd=new MenuAdd();
        menuAdd.showRemoveMenu(this,menu,siteDatas,mAdapter,SiteActivity.this,RAMMgr);
    }
}
