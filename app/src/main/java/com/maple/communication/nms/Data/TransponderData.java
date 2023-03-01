package com.maple.communication.nms.Data;


import com.maple.communication.nms.SnmpManager.GlobalVars;

/**
 * Created by 1012648 on 6/17/2016.
 */
public class TransponderData {
   public String ipAddress;
    public String community;
    public String macID;
    public String FWVer;
    public String HWVer;
    public String sysUpTime;

    public GlobalVars.onlineStatus status;
    public GlobalVars.transpoderType transType;
    public GlobalVars.transpoderAlarm alarm;
}

