package com.maple.communication.nms.DataManager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.maple.communication.nms.Data.SiteListItem;
import com.maple.communication.nms.Data.TransponderData;
import com.maple.communication.nms.Data.siteData;
import com.maple.communication.nms.DataBase.SQLiteDataManager;


import com.maple.communication.nms.Data.CMTSData;

import java.util.ArrayList;

/**
 * Created by Vineesh C on 17-07-2016.
 */
public class DataHandler {
    Context context;
    SQLiteDataManager sqLiteDataManager;

    // Site methods
    public boolean AddSite(String siteName){
        Boolean result = false;


        sqLiteDataManager.open();
        long siteID = sqLiteDataManager.checkAndInsertSiteTable(siteName);
        sqLiteDataManager.close();
        if (siteID!=0) result = true;

        return result;
    }

    public  DataHandler(Context cntx){
        context =cntx;
         sqLiteDataManager = new SQLiteDataManager(context);
    }
    public boolean DeleteSite(Long siteID){
        Boolean result = false;

        sqLiteDataManager.open();

        long res = sqLiteDataManager.deleteSiteTable(siteID);
        if (res <1) return false;

        res= sqLiteDataManager.deleteCmtsTable(siteID);
        if (res >0) {
            sqLiteDataManager.deleteTransponderTable(siteID);
        }
        sqLiteDataManager.close();
        if (res>0) result = true;

        return result;
    }
    // Site methods

    public boolean AddCMTS(CMTSData cmtsData){
        Boolean result = false;

        return result;
    }
    public boolean DeleteCMTS(long siteID){
        Boolean result = false;

        return result;
    }
    public boolean DeleteTransponder(String transponderIP){
        Boolean result = false;

        return result;
    }
    public boolean UpdateTransponderCommunity(String transponderIP){
        Boolean result = false;

        return result;
    }

    public ArrayList <SiteListItem> getSiteTable(){
        Boolean result = false;
        Cursor cursor =null;
        ArrayList <SiteListItem> siteList = new ArrayList<>();

        sqLiteDataManager.open();
        cursor = sqLiteDataManager.getSiteTable();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                int _id = cursor.getInt(cursor.getColumnIndex(SQLiteDataManager.ID));
                String siteName = cursor.getString(cursor.getColumnIndex(SQLiteDataManager.SITE_NAME));
              //  int id = cursor.getInt(cursor.getColumnIndex(SQLiteDataManager.ID));
                ArrayList<CMTSData> cmtsDatas = getCmtsData(_id);
                SiteListItem siteListItem = new SiteListItem(siteName, false, _id, cmtsDatas);
                 siteList.add(siteListItem);
                if (!cursor.isLast())
                    cursor.moveToNext();
               // Log.e("TAG", "SITE >>>>> " + siteName);

            }
        }

        sqLiteDataManager.close();
          return siteList;
    }

    ArrayList<CMTSData> getCmtsData(long siteID)
    {
        ArrayList<CMTSData> cmtsDatas=new ArrayList<>();
      //  SQLiteDataManager sqLiteDataManager = new SQLiteDataManager(this);
      //  sqLiteDataManager.open();

        Cursor cmtsCursor = sqLiteDataManager.getCmtsTableOfSite(siteID);
        if (cmtsCursor != null && cmtsCursor.getCount() > 0) {
            cmtsCursor.moveToFirst();

            for (int i = 0; i < cmtsCursor.getCount(); i++) {

                long cmtsID = cmtsCursor.getInt(cmtsCursor.getColumnIndex(SQLiteDataManager.ID));
                String name = cmtsCursor.getString(cmtsCursor.getColumnIndex(SQLiteDataManager.NAME));
                String ip = cmtsCursor.getString(cmtsCursor.getColumnIndex(SQLiteDataManager.IP));
                String readCommunity = cmtsCursor.getString(cmtsCursor.getColumnIndex(SQLiteDataManager.READ_COMMUNITY));
                String writeCommunity = cmtsCursor.getString(cmtsCursor.getColumnIndex(SQLiteDataManager.WRITE_COMMUNITY));
                long siteId = cmtsCursor.getLong(cmtsCursor.getColumnIndex(SQLiteDataManager.SITE_ID));

               // ArrayList<TransponderData> transponderDatas = new ArrayList<>();
                CMTSData cmtsData = new CMTSData(ip, readCommunity, writeCommunity, name, cmtsID, new ArrayList<TransponderData>(),siteId);
                cmtsDatas.add(cmtsData);
                if (!cmtsCursor.isLast())
                    cmtsCursor.moveToNext();
            }
        }

       // sqLiteDataManager.close();
        return cmtsDatas;
    }
    public void deleteSiteTable(String name){
        Boolean result = false;

        sqLiteDataManager.open();
        sqLiteDataManager.deleteSiteTable(name);

        //DeleteCMTS();
        sqLiteDataManager.close();
      //  return 0;
    }
    public void insertCmtsTable(String name,String ip,String readCommunity,String writeCommunity,long siteID,String macID,String status){
        sqLiteDataManager.open();
        sqLiteDataManager.insertCmtsTable(name, ip, readCommunity, writeCommunity, siteID, macID, status);
        sqLiteDataManager.close();
    }
    public void deleteCMTSfromTableForSite(long siteID){
        sqLiteDataManager.open();
        sqLiteDataManager.deleteCmtsTableForSite( siteID);
        sqLiteDataManager.close();
    }

    public void deleteCMTS(long cmtsID){
        sqLiteDataManager.open();
        sqLiteDataManager.deleteCmtsTable(cmtsID);
        sqLiteDataManager.deleteTransponderTable(cmtsID);
        sqLiteDataManager.close();
    }


    public String getTransponderCommunity(long cmtsID,String ipAddress){
        sqLiteDataManager.open();
       String community= sqLiteDataManager.getTransponderTable( ipAddress);
        sqLiteDataManager.close();
        return community;
    }

    public void insertTransponderTable(String ip,String readCommunity,String writeCommunity,long cmtsID,String macID,String status){

        String comunity=getTransponderCommunity(cmtsID,ip);
        sqLiteDataManager.open();
        if(!comunity.equals(""))
        {
            //Delete current entry
            sqLiteDataManager.deleteTransponderTable(ip);
        }
        sqLiteDataManager.insertTransponderTable(ip, readCommunity, writeCommunity, cmtsID, macID, status);
        sqLiteDataManager.close();
    }

}
