package com.maple.communication.nms.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




public class Database extends SQLiteOpenHelper 
{


	  private static final String	DATABASE_NAME 		= "NetworkDB";
	  private static final int 		DATABASE_VERSION	= 1;
	private final String siteTableQuery = "create table "+SQLiteDataManager.SITE_TABLE+"("
			+ SQLiteDataManager.ID+" integer primary key autoincrement,"+SQLiteDataManager.SITE_NAME+" text not null)";
	private final String cmtsTableQuery = "create table "+SQLiteDataManager.CMTS_TABLE
			+"("+SQLiteDataManager.ID+" integer primary key autoincrement,"
			+SQLiteDataManager.NAME+" text,"
			+SQLiteDataManager.IP+" text,"
			+SQLiteDataManager.READ_COMMUNITY+
			" text,"+SQLiteDataManager.WRITE_COMMUNITY
			+" text,"+SQLiteDataManager.SITE_ID
			+" integer,"+SQLiteDataManager.MAC_ADDRESS+" text,"
			+SQLiteDataManager.STATUS+" text)";

	private final String transponderTableQuery = "create table "+SQLiteDataManager.TRANSPONDER_TABLE+"("+SQLiteDataManager.ID+" integer primary key autoincrement,"
			+ SQLiteDataManager.IP+" text,"
			+ SQLiteDataManager.READ_COMMUNITY+" text,"
			+ SQLiteDataManager.WRITE_COMMUNITY+" text,"
			+ SQLiteDataManager.CMTS_ID+" integer,"
			+ SQLiteDataManager.MAC_ADDRESS+" text,"
			+ SQLiteDataManager.STATUS+" text)";

	  


	/**
	 * Pelco database constructor
	 * 
	 * @param context
	 *            : Application context
	 */
	public Database(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	/**
	 * Create the tables
	 * 
	 * @param SQLiteDatabase
	 * @note table creation queries
	 */
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(siteTableQuery);
		database.execSQL(cmtsTableQuery);
		database.execSQL(transponderTableQuery);
	}


	/**
	 * Handling the database upgrade events
	 * 
	 * @param SQLiteDatabase, oldVersion, newVersion
	 * @note Executes when database is changed
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}
	
}