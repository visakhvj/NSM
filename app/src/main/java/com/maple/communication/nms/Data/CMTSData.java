package com.maple.communication.nms.Data;

import java.util.ArrayList;

/**
 * Created by 1012648 on 6/17/2016.
 */
public class CMTSData extends TransponderData {
    public long siteID;
    public String ipAddress;
    public String readCommunity;
    public String writeCommunity;
    public String name;
    public long _id;
    public ArrayList<TransponderData> transDataList;
    public boolean isUpdated=false;

    public CMTSData(String ipAddress, String readCommunity,String writeCommunity, String name, long _id, ArrayList<TransponderData> transDataList,long siteID) {
        this.ipAddress = ipAddress;
        this.readCommunity = readCommunity;
        this.writeCommunity=writeCommunity;
        this.name = name;
        this._id = _id;
        this.siteID = siteID;
        this.transDataList = transDataList;
        isUpdated=false;
    }
}
