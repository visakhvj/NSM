package com.maple.communication.nms.SnmpManager;

/**
 * Created by Vineesh C on 19-06-2016.
 */
public class GlobalVars {
   public static enum  transpoderType{
        DM,DSM3,SMG,UNKNOWN;
    }
   public static enum  transpoderAlarm{
        NORMAL,MINOR,MAJOR,UNKNOWN;
    }
   public static enum  onlineStatus{
        ONLINE,OFFLINE;
    }
    public static enum  snmpRequest{
        IN_PROGRESS,COMPLETED;
    }

}
