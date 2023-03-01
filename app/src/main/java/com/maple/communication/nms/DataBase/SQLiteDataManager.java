package com.maple.communication.nms.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.util.Log;

import com.maple.communication.nms.Data.CMTSData;

import java.util.ArrayList;

/**
 * Created by Vineesh C on 13-08-2016.
 */
public class SQLiteDataManager {
    public static final String SITE_TABLE="Site";
    public static final String CMTS_TABLE="Cmts";
    public static final String TRANSPONDER_TABLE="Transponder";

    public static final String ID="_id";
    public static final String SITE_NAME="SiteName";
    public static final String IP="IP";
    public static final String NAME="Name";
    public static final String READ_COMMUNITY="ReadComm";
    public static final String WRITE_COMMUNITY="WriteComm";
    public static final String SITE_ID="SiteID";
    public static final String MAC_ADDRESS="MacAddr";
    public static final String STATUS="status";

    public static final String CMTS_ID="CmtsID";

    private SQLiteDatabase _database;
    private Database _dbHelper;
    private Context ctx;

    /**
     * The constructor
     *
     * @param context : Application context
     * @note creates the SQLiteDatabase
     */
    public SQLiteDataManager(Context context)
    {
        this.ctx = context;
        _dbHelper = new Database(context);
    }


    /**
     * Open the database
     *
     * @throws SQLException
     * @note opens the Database
     */
    public void open() throws SQLException
    {
        try
        {
            _database = _dbHelper.getWritableDatabase();
        }
        catch (SQLiteDatabaseLockedException e)
        {
            _dbHelper = new Database(ctx);
            _dbHelper.close();
            _database = _dbHelper.getReadableDatabase();
        }
    }

    public long checkAndInsertSiteTable(String name)
    {
        boolean isExist = false;
        Cursor values = getSiteTable(name);
        if (values.getCount() > 0)
        {
            isExist = true;
            values.close();
        }
        if(!isExist)
            return insertSiteTable(name);
        else
            return -1;
    }



    public Cursor getSiteTable(String name)
    {
        Cursor cursor = _database.query(
                SITE_TABLE,null,
                SITE_NAME + "=?", new String[]
                        { String.valueOf(name) }, null, null, null, null);
        return cursor;
    }

    public Cursor getSiteTable()
    {
        Cursor cursor = _database.query(
                SITE_TABLE, null, null, null,
                null, null, null, null);


        return cursor;
    }

    public long insertSiteTable(String name)
    {
        ContentValues values = createContentValuesSiteTable(name);
        return _database.insert(SITE_TABLE,
                null, values);
    }
    ContentValues createContentValuesSiteTable(String name)
    {
        ContentValues values = new ContentValues();
        values.put(SITE_NAME, name);
        return values;
    }
    public long insertCmtsTable(String name,String ip,String readCommunity,String writeCommunity,long siteID,String macID,String status)
    {
        ContentValues values = createContentValuesCmtsTable(name, ip, readCommunity, writeCommunity, siteID, macID, status);
        return _database.insert(CMTS_TABLE,
                null, values);
    }


    public int updateCmtsTable(String name,String ip,String readCommunity,String writeCommunity,long siteID,String macID,String status, long _id)
    {
        ContentValues values = createContentValuesCmtsTable(name, ip, readCommunity, writeCommunity, siteID, macID, status);
        return _database.update(CMTS_TABLE,
                values, ID + " =" + _id
                , null);
    }



    ContentValues createContentValuesCmtsTable(String name,String ip,String readCommunity,String writeCommunity,long siteID,String macID,String status)
    {
        ContentValues values = new ContentValues();
        values.put(NAME,name);
        values.put(IP, ip);
        values.put(READ_COMMUNITY,readCommunity);
        values.put(WRITE_COMMUNITY, writeCommunity);
        values.put(SITE_ID,siteID);
        values.put(MAC_ADDRESS,macID);
        values.put(STATUS, status);
        return values;
    }

    public int deleteSiteTable(String name)
    {
        Log.e(">>>>", "Here You are ");
        int id;
        Cursor cursor = getSiteTable( name);
        if (cursor.getCount() > 0)
        {

            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(SQLiteDataManager.ID));
            deleteSiteTable(id);

          //  cursor.getInt(cursor.getColumnIndex(SQLiteDataManager.ID));
        //   id = cursor.getInt(cursor.getColumnIndex(SQLiteDataManager.ID));
        }
        else{
            Log.e(">>>>",">>>> Curson size 0");
            return 0;
        }


      /* return _database.delete(SITE_TABLE,
                ID + " = ?", new String[]
                        {String.valueOf(id)});*/
        return 0;
    }


    public int deleteSiteTable(long _id)
    {


        long res = deleteCmtsTableSite(_id);

        return _database.delete(SITE_TABLE,
                ID + " = ?", new String[]
                        {String.valueOf(_id)});
    }

    public int deleteCmtsTableForSite(long siteID)
    {
        int a = _database.delete(CMTS_TABLE,
                SITE_ID + " = ?", new String[]
                        {String.valueOf(siteID)});
        Log.e("","DEL --- RESULT - "+ a);
        return a;
    }
    public int deleteCmtsTable(long _id)
    {
        int a = _database.delete(CMTS_TABLE,
                ID + " = ?", new String[]
                        {String.valueOf(_id)});
        Log.e("","DEL --- RESULT - "+ a);
        return a;
    }

    public int deleteCmtsTableSite(long _id)
    {
        Cursor cmts=getCmtsTableOfSite(_id);
        if(cmts.getCount()>0)
        {
            cmts.moveToFirst();
            for(int i=0;i<cmts.getCount();i++)
            {
                long cmtsID=cmts.getInt(cmts.getColumnIndex(ID));
                deleteCmtsTable(cmtsID);
                deleteTransponderTable(cmtsID);
                if(!cmts.isLast())
                    cmts.moveToNext();
            }
        }
        int a = _database.delete(CMTS_TABLE,
                SITE_ID + " = ?", new String[]
                        {String.valueOf(_id)});
        Log.e("","DEL --- RESULT - "+ a);
        return a;
    }

    public int deleteTransponderTable(long _id)
    {
        return _database.delete(TRANSPONDER_TABLE,
                CMTS_ID + " = ?", new String[]
                        {String.valueOf(_id)});
    }


    /**
     * Closes the database
     *
     * @note closes the database
     */
    public void close()
    {
        _dbHelper.close();
    }

    public Cursor getCmtsTable(long _id)
    {

        Cursor cursor = _database.query(
                CMTS_TABLE, null,
                ID + "=?", new String[]
                        { String.valueOf(_id) }, null, null, null, null);
        return cursor;
    }
    public Cursor getCmtsTableOfSite(long siteID)
    {

        Cursor cursor = _database.query(
                CMTS_TABLE, null,
                SITE_ID + "=?", new String[]
                        { String.valueOf(siteID) }, null, null, null, null);
        return cursor;
    }


    public long insertTransponderTable(String ip,String readCommunity,String writeCommunity,long cmtsID,String macID,String status)
    {
        ContentValues values = createContentValuesTransponderTable(ip,readCommunity,writeCommunity,cmtsID,macID,status);
        return _database.insert(TRANSPONDER_TABLE,
                null, values);
    }


    public int updateTransponderTable(String ip,String readCommunity,String writeCommunity,long cmtsID,String macID,String status,long _id)
    {
        ContentValues values = createContentValuesTransponderTable(ip,readCommunity,writeCommunity,cmtsID,macID,status);
        return _database.update(TRANSPONDER_TABLE,
                values,ID + " =" + _id
                , null);
    }

    public int deleteTransponderTable(String  ip)
    {
        return _database.delete(TRANSPONDER_TABLE,
                IP + " = ?", new String[]
                        {ip});
    }



    public String getTransponderTable(String ipAddress)
    {

        Cursor cursor = _database.query(
                TRANSPONDER_TABLE, null,
                IP + "=?", new String[]
                        { String.valueOf(ipAddress) }, null, null, null, null);

        String community="";
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            community=cursor.getString(cursor.getColumnIndex(READ_COMMUNITY));
        }
        cursor.close();
        return community;
    }

    public Cursor getTransponderTable(long _id)
    {

        Cursor cursor = _database.query(
                TRANSPONDER_TABLE, new String[]
                        { IP,READ_COMMUNITY,WRITE_COMMUNITY,CMTS_ID,MAC_ADDRESS,STATUS},
                ID + "=?", new String[]
                        { String.valueOf(_id) }, null, null, null, null);
        return cursor;
    }


    public Cursor getTransponderTable()
    {
        String countQuery = "SELECT  * FROM "
                + TRANSPONDER_TABLE;
        Cursor cursor = _database.rawQuery(countQuery, null);
        return cursor;
    }



    public int getTransponderTableCount()
    {
        String countQuery = "SELECT  * FROM "
                + TRANSPONDER_TABLE;
        Cursor cursor = _database.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }



    ContentValues createContentValuesTransponderTable(String ip,String readCommunity,String writeCommunity,long cmtsID,String macID,String status)
    {
        ContentValues values = new ContentValues();
        values.put(IP, ip);
        values.put(READ_COMMUNITY,readCommunity);
        values.put(WRITE_COMMUNITY,writeCommunity);
        values.put(CMTS_ID,cmtsID);
        values.put(MAC_ADDRESS,macID);
        values.put(STATUS,status);
        return values;
    }


}
